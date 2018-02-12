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

package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import org.biiig.dmgm.api.db.CachedGraph;
import org.biiig.dmgm.api.db.QueryElements;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DistinctEdgeLabels;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DistinctVertexLabels;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CharacteristicLabels extends PreprocessorBase {

  CharacteristicLabels(float minSupport) {
    super(minSupport);
  }

  @Override
  public List<CachedGraph> apply(List<CachedGraph> collection, QueryElements db) {
    Set<Integer> frequentVertexLabels = getCategoryFrequentLabels(collection, new DistinctVertexLabels());

    List<CachedGraph> vertexPrunedCollection = collection
      .stream()
//      .map(new PruneVertices(frequentVertexLabels))
      .collect(Collectors.toList());

    Set<Integer> frequentEdgeLabels =
      getCategoryFrequentLabels(vertexPrunedCollection, new DistinctEdgeLabels());

    return vertexPrunedCollection
      .stream()
//      .map(new PruneEdges(frequentEdgeLabels))
      .collect(Collectors.toList());
  }

  private Set<Integer> getCategoryFrequentLabels(List<CachedGraph> collection, Function<CachedGraph, Stream<Integer>> labelSelector) {
    Map<Integer, List<CachedGraph>> categorizedGraphs = collection
      .stream()
      .collect(new GroupByKeyListValues<>(CachedGraph::getLabel, Function.identity()));

    return categorizedGraphs
      .values()
      .stream()
      .map(g -> getFrequentLabels(g.stream(), labelSelector, (int) (g.size() * minSupport)))
      .flatMap(Collection::stream)
      .collect(Collectors.toSet());
  }

}
