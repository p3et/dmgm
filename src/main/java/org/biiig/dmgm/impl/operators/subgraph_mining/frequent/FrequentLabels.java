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

package org.biiig.dmgm.impl.operators.subgraph_mining.frequent;

import org.biiig.dmgm.api.db.CachedGraph;
import org.biiig.dmgm.api.db.QueryElements;
import org.biiig.dmgm.impl.operators.subgraph.FilterVerticesAndEdgesByLabel;
import org.biiig.dmgm.impl.operators.subgraph_mining.characteristic.PreprocessorBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DistinctEdgeLabels;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DistinctVertexLabels;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FrequentLabels extends PreprocessorBase {

  public FrequentLabels(float minSupport) {
    super(minSupport);
  }

  @Override
  public List<CachedGraph> apply(List<CachedGraph> collection, QueryElements builder) {
    Integer minSupportAbsolute = Math.round(collection.size() * minSupport);

    Set<Integer> frequentVertexLabels =
      getFrequentLabels(collection.stream(), new DistinctVertexLabels(), minSupportAbsolute);

    List<CachedGraph> vertexPrunedCollection = collection
        .stream()
        .map(new FilterVerticesAndEdgesByLabel(frequentVertexLabels::contains, i -> true, true))
        .collect(Collectors.toList());

    Set<Integer> frequentEdgeLabels =
      getFrequentLabels(vertexPrunedCollection.stream(), new DistinctEdgeLabels(), minSupportAbsolute);

    return vertexPrunedCollection
      .stream()
      .map(new FilterVerticesAndEdgesByLabel(i -> true, frequentEdgeLabels::contains, true))
      .collect(Collectors.toList());
  }
}
