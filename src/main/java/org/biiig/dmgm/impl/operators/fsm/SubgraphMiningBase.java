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

package org.biiig.dmgm.impl.operators.fsm;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.config.DMGMConstants;
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.impl.operators.DMGMOperatorBase;
import org.biiig.dmgm.impl.operators.fsm.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.fsm.common.GrowAllChildren;
import org.biiig.dmgm.impl.operators.fsm.common.WithCachedGraph;
import org.biiig.dmgm.impl.operators.fsm.common.WithEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCode;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class SubgraphMiningBase<G extends WithCachedGraph, E extends WithEmbedding, S>
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
  protected final int patternLabel;
  /**
   * Encoded label for the result subgraph collection.
   */
  protected final int collectionLabel;

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
  }

  @Override
  public Long apply(Long inputCollectionId) {
    Collection<CachedGraph> input = db.getCachedCollection(inputCollectionId);

    // create an index of graphs specific to the mining variant
    Map<Long, G> graphIndex = preProcess(input)
      .collect(Collectors.toMap(
        g -> g.getGraph().getId(),
        Function.identity()
      ));

    // init aggregate single edge patterns
    Map<DFSCode, List<E>> patternEmbeddings = getParallelizableStream(graphIndex.values())
      .flatMap(this::initializeSingleEdgePatterns)
      .collect(new GroupByKeyListValues<>(Pair::getKey, Pair::getValue));

    Set<DFSCode> frequentPatterns = Sets.newConcurrentHashSet();

    // identify frequent patterns
    List<Pair<DFSCode, S>> patternSupport = addSupportAndFilter(patternEmbeddings)
      .peek(p -> frequentPatterns.add(p.getKey()))
      .collect(Collectors.toList());

    long[] graphIds = output(patternSupport, patternEmbeddings);

    int edgeCount = 1;
    while (!frequentPatterns.isEmpty() && edgeCount < maxEdgeCount) {

      patternEmbeddings = getParallelizableStream(patternEmbeddings.entrySet())
        .filter(p -> frequentPatterns.contains(p.getKey()))
        // => (pattern, embedding...)
        .map(parentEmbeddings -> growPatternChildren(graphIndex, parentEmbeddings))
        // => map: pattern -> embedding...
        .flatMap(m -> m.entrySet().stream())
        // => (pattern, embedding...)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

      // reset frequent patterns
      frequentPatterns.clear();

      patternSupport = addSupportAndFilter(patternEmbeddings)
        .peek(p -> frequentPatterns.add(p.getKey()))
        .collect(Collectors.toList());

      graphIds = ArrayUtils.addAll(graphIds, output(patternSupport, patternEmbeddings));

      edgeCount++;
    }

    return db.createCollection(collectionLabel, graphIds);
  }

  /**
   * Grow all children of a frequent pattern (parent).
   * This method is executed in parallel.
   *
   * @param graphIndex
   * @param parentEmbeddings (parent, embedding...)
   * @return map: child -> embedding...
   */
  private Map<DFSCode, List<E>> growPatternChildren(Map<Long, G> graphIndex, Map.Entry<DFSCode, List<E>> parentEmbeddings) {
    DFSCode parent = parentEmbeddings.getKey();
    List<Pair<DFSCode, E>> children = Lists.newArrayList();

    GrowAllChildren<G, E> growAllChildren = new GrowAllChildren<>(parent, getEmbeddingFactory());

    List<E> embeddings = parentEmbeddings.getValue();
    embeddings.sort(Comparator.comparing(e -> e.getEmbedding().getGraphId()));

    Iterator<E> iterator = embeddings.iterator();
    G withGraph = null;

    while (iterator.hasNext()) {
      E withEmbedding = iterator.next();
      DFSEmbedding embedding = withEmbedding.getEmbedding();
      long graphId = embedding.getGraphId();

      if (withGraph == null || graphId != withGraph.getGraph().getId())
        withGraph = graphIndex.get(graphId);

      growAllChildren.addChildren(withGraph, embedding, children);
    }

    Map<DFSCode, List<E>> childEmbeddings = children
      .stream()
      .collect(new GroupByKeyListValues<>(Pair::getKey, Pair::getValue));

    return childEmbeddings;
  }
}
