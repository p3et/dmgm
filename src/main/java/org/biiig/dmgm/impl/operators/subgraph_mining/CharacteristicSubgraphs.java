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

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.impl.operators.fsm.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCode;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.GrowAllChildren;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.InitializeParents;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.IsMinimal;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * min frequency is with regard to f
 */
public class CharacteristicSubgraphs<G extends CachedGraph> extends SubgraphMiningBase<G, Map<Float, Integer>> {

  private int categoryKey;
  private int defaultCategory;

  @Override
  public Long apply(Long inputCollectionId) {
    // apply specific preprocessing and index graphs by their id
    Stream<G> input = preProcess(inputCollectionId);

    Map<Integer, Map<Long, G>> categorizedGraphs = input
      .collect(new GroupByKeyListValues<>(
        g -> {
          String category = db.getString(g.getId(), categoryKey);
          return category == null ? defaultCategory : db.encode(category);
        },
        Function.identity()
      ))
      .entrySet()
      .parallelStream()
      .collect(Collectors.toMap(
        Map.Entry::getKey,
        e -> e.getValue()
          .stream()
          .collect(Collectors.toMap(
            CachedGraph::getId,
            Function.identity()
          ))
      ));

    Map<Integer, Long> categoryMinSupport = categorizedGraphs
      .entrySet()
      .stream()
      .collect(Collectors.toMap(
        Map.Entry::getKey,
        e -> (long) (e.getValue().size() * minSupportRel)
      ));

    int edgeCount = 1;
    Set<DFSCode> frequentPatterns = Sets.newHashSet();
    Map<Integer, Map<DFSCode, Pair<List<DFSEmbedding>, Long>>> parentEmbeddingsSupport = Maps.newHashMap();

    for (Map.Entry<Integer, Map<Long, G>> entry : categorizedGraphs.entrySet()) {
      int category = entry.getKey();
      long minSupport = categoryMinSupport.get(category);

      // turn edges into 1-edge patterns and embeddings
      Stream<Pair<DFSCode,DFSEmbedding>> candidates = getParallelizableStream(entry.getValue().values())
        .flatMap(new InitializeParents<>(patternLabel));

      // aggregate support and filter by support

      Map<DFSCode, List<DFSEmbedding>> patternEmbeddings = candidates
        .collect(new GroupByKeyListValues<>(Pair::getKey, Pair::getValue));

      Map<DFSCode, Pair<List<DFSEmbedding>, Long>> patternEmbeddingsSupport =
        getParallelizableStream(patternEmbeddings.entrySet())
        .collect(Collectors.toMap(
          Map.Entry::getKey,
          e -> new Pair<>(
            e.getValue(),
            e.getValue()
              .stream()
              .mapToLong(DFSEmbedding::getGraphId)
              .distinct()
              .count()
          )));

      parentEmbeddingsSupport.put(category, patternEmbeddingsSupport);

      List<DFSCode> categoryFrequentPatterns = getParallelizableStream(patternEmbeddingsSupport.entrySet())
        .filter(e -> e.getValue().getValue() >= minSupport)
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());

      frequentPatterns.addAll(categoryFrequentPatterns);
    }

    Collection<Triple<DFSCode, Integer, Long>> result = new ConcurrentLinkedQueue<>();

    for (int category : categorizedGraphs.keySet()) {
      Map<Long, G> categoryGraphs = categorizedGraphs.get(category);
      Map<DFSCode, Pair<List<DFSEmbedding>, Long>> patternEmbeddingsSupport = parentEmbeddingsSupport.get(categoryKey);

      parentEmbeddingsSupport = getParallelizableStream(patternEmbeddingsSupport.entrySet())
        .filter(e -> frequentPatterns.contains(e.getKey()))
        .peek(e -> result.add(new ImmutableTriple<>(e.getKey(), category, e.getValue().getValue())))
        .map(e -> new Pair<>(e.getKey(), e.getValue().getKey()))
        .flatMap(new GrowAllChildren<>(categoryGraphs))

      parentEmbeddingsSupport.put(category, patternEmbeddingsSupport);

    }









    // output 1-edge patterns
    List<Pair<Pair<DFSCode, List<DFSEmbedding>>, S>> parents = aggregated.collect(Collectors.toList());
    long[] graphIds = output(parents, input, afo);

    while (edgeCount < maxEdgeCount && !parents.isEmpty()) {
      // grow k+1-edge candidates
      candidates = getParallelizableStream(parents)
        .map(Pair::getKey)
        .flatMap(new GrowAllChildren<>(input));
      // aggregate embeddings and support
      parents = afo.aggregateAndFilter(candidates)
        // verify that DFS code is minimal
        .filter(e -> new IsMinimal().test(e.getKey().getKey()))
        .collect(Collectors.toList());
      // add k+1-edge patterns to output
      graphIds = ArrayUtils.addAll(graphIds, output(parents, input, afo));

      edgeCount++;
    }

    return db.createCollection(collectionLabel, graphIds);
  }

}
