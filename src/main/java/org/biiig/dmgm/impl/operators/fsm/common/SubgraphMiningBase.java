/*
 * This file is part of Directed Multigraph Miner (DMGM).
 *
 * DMGM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DMGM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DMGM. If not, see <http://www.gnu.org/licenses/>.
 */

package org.biiig.dmgm.impl.operators.fsm.common;

import com.google.common.collect.Lists;
import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.config.DMGMConstants;
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.impl.model.AdjacencyList;
import org.biiig.dmgm.impl.operators.DMGMOperatorBase;
import org.biiig.dmgm.impl.operators.fsm.characteristic.WithEmbeddingAndCategory;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SubgraphMiningBase<G extends WithGraph, E extends WithEmbeddingAndCategory, S>
  extends DMGMOperatorBase implements SubgraphMining<G, E, S> {

  /**
   * Minimum support threshold.
   * For example, if set to 0.5,
   * all subgraph occurring in at least 50% of graphs of a collection are considered to be frequent.
   */
  protected final float minSupportRel;
  /**
   * Maximum number of edges of extracted subgraphs.
   * Important parameter to limit the result size and total runtime.
   */
  protected final int maxEdgeCount;

  /**
   * Encoded label for result subgraphs.
   */
  private final int patternLabel;
  /**
   * Encoded label for the result subgraph collection.
   */
  private final int collectionLabel;
  /**
   * Property key to store the support value of a pattern.
   */
  private final int supportKey;
  /**
   * Property key to store the canonical label (DFS code) of a pattern.
   */
  private final int dfsCodeKey;

  /**
   * Constructor.
   *
   * @param db database
   * @param parallel flag to enable parallel execution
   * @param minSupportRel minimum support threshold
   * @param maxEdgeCount maximum result edge count
   */

  protected SubgraphMiningBase(PropertyGraphDB db, boolean parallel, float minSupportRel, int maxEdgeCount) {
    super(parallel, db);
    this.minSupportRel = minSupportRel;
    this.maxEdgeCount = maxEdgeCount;
    this.patternLabel = db.encode(DMGMConstants.Labels.FREQUENT_SUBGRAPH);
    this.collectionLabel = db.encode(DMGMConstants.Labels.FREQUENT_SUBGRAPHS);
    this.supportKey = db.encode(DMGMConstants.PropertyKeys.SUPPORT);
    this.dfsCodeKey = db.encode(DMGMConstants.PropertyKeys.DFS_CODE);
  }

  @Override
  public Long apply(Long inputCollectionId) {
    Collection<CachedGraph> input = database.getCachedCollection(inputCollectionId);

    input = input.stream().map(g -> new AdjacencyList(
      g.getId(),
      g.getLabel(),
      g.getVertexLabels(),
      g.getEdgeLabels(),
      g.getSourceIds(),
      g.getTargetIds()
      )).collect(Collectors.toList());

    S minSupportAbsolute = getMinSupportAbsolute(input, minSupportRel);

    // create an index of graphs specific to the mining variant
    Stream<G> preProcessed = preProcess(input);
    Map<Long, G> graphIndex = preProcessed
      .collect(Collectors.toMap(g -> g.getGraph().getId(), Function.identity()));

    // init aggregate single edge patterns
    Map<DFSCode, List<E>> patternEmbeddings = getParallelizableStream(graphIndex.values())
      .flatMap(new InitializeSingleEdgePatterns<>(patternLabel, getEmbeddingFactory()))
      .collect(new GroupByKeyListValues<>(Pair::getKey, Pair::getValue));

    // identify frequent patterns
    List<Pair<DFSCode, S>> frequentPatterns = addSupportAndFilter(patternEmbeddings, minSupportAbsolute, parallel)
      .collect(Collectors.toList());

    long[] graphIds = output(frequentPatterns, patternEmbeddings, minSupportAbsolute);

    int edgeCount = 1;
    while (!frequentPatterns.isEmpty() && edgeCount < maxEdgeCount) {
      Map<DFSCode, List<E>> finalPatternEmbeddings = patternEmbeddings;
      patternEmbeddings = getParallelizableStream(frequentPatterns)
        // => (pattern, embedding...)
        .map(Pair::getKey)
        .map(p -> growPatternChildren(p, finalPatternEmbeddings.get(p), graphIndex))
        // => map: pattern -> embedding...
        .flatMap(m -> m.entrySet().stream())
        // => (pattern, embedding...)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

      frequentPatterns = addSupportAndFilter(patternEmbeddings, minSupportAbsolute, false)
        // additionally verify non-minimal DFS codes
        .filter(p -> new IsMinimal().test(p.getKey()))
//        .peek(p -> System.out.println("\t" + p.getKey().toString(database) + " : " + p.getValue() + " - " ))
        .collect(Collectors.toList());

      graphIds = ArrayUtils.addAll(graphIds, output(frequentPatterns, patternEmbeddings, minSupportAbsolute));

      edgeCount++;
    }

    return database.createCollection(collectionLabel, graphIds);
  }

  /**
   * Grow all children of a frequent pattern (parent).
   * This method is executed in parallel, i.e., its body is executed sequentially.
   * Note: Since most of the complexity and memory usage of FSM happens here
   * the code is optimized to performance and intently breaking with functional concepts.
   *
   *
   * @return map: child -> embedding...
   */
  private Map<DFSCode, List<E>> growPatternChildren(
    DFSCode parent, List<E> embeddings, Map<Long, G> graphIndex) {

    // set parent and pattern growth logic
    GrowAllChildren<G, E> growAllChildren = new GrowAllChildren<>(parent, getEmbeddingFactory());


    // A single list for all children.
    // The list is passed through the pattern growth process.
    List<Pair<DFSCode, E>> children = Lists.newArrayList();

    embeddings.sort(Comparator.comparing(e -> e.getEmbedding().getGraphId()));
    Iterator<E> iterator = embeddings.iterator();
    G withGraph = null;

//    System.out.println(parent + " : " + embeddings.size());

    // for each embedding
    while (iterator.hasNext()) {
      E withEmbedding = iterator.next();
      DFSEmbedding embedding = withEmbedding.getEmbedding();
      long graphId = embedding.getGraphId();

      // load new graph if required
      if (withGraph == null || graphId != withGraph.getGraph().getId())
        withGraph = graphIndex.get(graphId);

//      System.out.println(parent + " "  + graphId + " " + embedding);

      // grow all children and add to list
      growAllChildren.addChildren(withGraph, embedding, children);
    }

    // aggregate here since the same child may not be grown from different parents
    return children
      .stream()
      .collect(new GroupByKeyListValues<>(Pair::getKey, Pair::getValue));
  }

  @Override
  public long getAbsoluteSupport(long count, float rel) {
    return Math.round((double) count * rel);
  }

  @Override
  public int getSupportKey() {
    return supportKey;
  }

  @Override
  public int getDfsCodeKey() {
    return dfsCodeKey;
  }
}
