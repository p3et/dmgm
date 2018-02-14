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
import org.biiig.dmgm.impl.operators.fsm.common.DFSCode;
import org.biiig.dmgm.impl.operators.fsm.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SupportSpecialization;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Methods that distinguish simple vs. generalized and frequent vs. category frequent subgraph mining.
 *
 * @param <G> type of cached graph (simple or specializable)
 * @param <S> type of support (frequency or category->frequency map)
 */
public interface SubgraphMiningVariant<G extends CachedGraph, S> {

  // SIMPLE VS. GENERALIZED

  /**
   * Prepare the input graph collection for the mining process.
   * This method differs for preProcess and simple subgraph mining.
   *
   * @param inputCollectionId id of the input graph collection
   * @return map: graphId -> cached graph
   */
  Stream<G> preProcess(Long inputCollectionId);

  /**
   *
   *
   *
   * @param indexedGraphs
   * @param filtered
   * @param afo
   * @return
   */
  List<Pair<Pair<DFSCode,List<DFSEmbedding>>, S>> postProcess(
    Map<Long, G> indexedGraphs, List<Pair<Pair<DFSCode, List<DFSEmbedding>>, S>> filtered, SupportSpecialization<S> afo);

  // FREQUENT VS. CATEGORY FREQUENT

  SupportSpecialization<S> getSupportSpecialization(Map<Long, G> indexedGraphs, PropertyGraphDB db, S minSupportAbs, boolean parallel);

  Map<Integer, Long> getVertexLabelSupport(List<CachedGraph> input);

  S getMinSupportAbsolute(Map<Long, G> indexedGraphs, float minSupportRel);
}
