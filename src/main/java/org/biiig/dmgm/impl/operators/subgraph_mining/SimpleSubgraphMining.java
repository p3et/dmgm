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
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCode;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SupportSpecialization;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class SimpleSubgraphMining extends SubgraphMiningBase<CachedGraph, Long> {
  public SimpleSubgraphMining(PropertyGraphDB db, boolean parallel, float minSupportRel, int maxEdgeCount) {
    super(db, parallel, minSupportRel, maxEdgeCount);
  }

  @Override
  public Map<Long, CachedGraph> preProcess(Long inputCollectionId) {
    return getParallelizableStream(db.getCachedCollection(inputCollectionId))
      .collect(Collectors.toMap(CachedGraph::getId, Function.identity()));
  }

  @Override
  public List<Pair<Pair<DFSCode, List<DFSEmbedding>>, Long>> postProcess(
    Map<Long, CachedGraph> indexedGraphs,
    List<Pair<Pair<DFSCode, List<DFSEmbedding>>, Long>> filtered,
    SupportSpecialization<Long> afo
  ) {
    return filtered;
  }
}
