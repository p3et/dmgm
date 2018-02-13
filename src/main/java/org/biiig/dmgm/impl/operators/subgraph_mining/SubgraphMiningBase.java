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

package org.biiig.dmgm.impl.operators.subgraph_mining;

import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.config.DMGMConstants;
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.api.operators.CollectionToCollectionOperator;
import org.biiig.dmgm.impl.operators.DMGMOperatorBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCode;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.GrowAllChildren;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.InitializeParents;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.IsMinimal;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SupportSpecialization;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Superclass of operators based on Frequent Subgraph Mining.
 * @see <a href = "https://www.cambridge.org/core/journals/knowledge-engineering-review/article/a-survey-of-frequent-subgraph-mining-algorithms/A58904230A6680001F17FCE91CB8C65F">
 *   A survey of frequent subgraph mining algorithms</a>
 *
 * @param <G> graph type, e.g., specializable graph
 * @param <S> support type, e.g., long for simple frequency based mining
 */
public abstract class SubgraphMiningBase<G extends CachedGraph, S> extends DMGMOperatorBase
  implements CollectionToCollectionOperator, SubgraphMiningVariant<G, S> {

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
    // apply specific preprocessing and index graphs by their id
    Map<Long, G> indexedGraphs = preProcess(inputCollectionId);

    int edgeCount = 1;

    // turn edges into 1-edge patterns and embeddings
    Stream<Pair<DFSCode,DFSEmbedding>> candidates = getParallelizableStream(indexedGraphs.values())
      .flatMap(new InitializeParents<>(patternLabel));
    // aggregate support and filter by support
    S minSupportAbsolute = getMinSupportAbsolute(indexedGraphs, minSupportRel);
    SupportSpecialization<S> afo = getSupportSpecialization(indexedGraphs, db, minSupportAbsolute, parallel);
    Stream<Pair<Pair<DFSCode,List<DFSEmbedding>>, S>> aggregated = afo.aggregateAndFilter(candidates);

    // output 1-edge patterns
    List<Pair<Pair<DFSCode, List<DFSEmbedding>>, S>> parents = aggregated.collect(Collectors.toList());
    long[] graphIds = output(parents, indexedGraphs, afo);

    while (edgeCount < maxEdgeCount && !parents.isEmpty()) {
      // grow k+1-edge candidates
      candidates = getParallelizableStream(parents)
        .map(Pair::getKey)
        .flatMap(new GrowAllChildren<>(indexedGraphs));
      // aggregate embeddings and support
      parents = afo.aggregateAndFilter(candidates)
        // verify that DFS code is minimal
        .filter(e -> new IsMinimal().test(e.getKey().getKey()))
        .collect(Collectors.toList());
      // add k+1-edge patterns to output
      graphIds = ArrayUtils.addAll(graphIds, output(parents, indexedGraphs, afo));

      edgeCount++;
    }

    return db.createCollection(collectionLabel, graphIds);
  }

  /**
   * Output a partial result.
   *
   * @param result filtered partial result
   * @param input
   * @param supportSpecialization
   * @return database graph ids of the output
   */
  private long[] output(List<Pair<Pair<DFSCode, List<DFSEmbedding>>, S>> result, Map<Long, G> input, SupportSpecialization<S> supportSpecialization) {
    result = postProcess(input, result, supportSpecialization);

    return getParallelizableStream(result)
      .map(supportSpecialization::output)
      .flatMapToLong(LongStream::of)
      .toArray();
  }

  /**
   * Stream a collection according to the parallel execution flag.
   *
   * @param collection collection to stream
   * @param <T> element type
   * @return sequential or parallel stream
   */
  <T> Stream<T> getParallelizableStream(Collection<T> collection) {
    return parallel ?
      collection.parallelStream() :
      collection.stream();
  }

  @Override
  public Map<Integer, Long> getVertexLabelSupport(List<CachedGraph> input) {
    return (getParallelizableStream(input))
      .flatMapToInt(g -> IntStream
        .of(g.getVertexLabels())
        .distinct())
      .boxed()
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
  }
}
