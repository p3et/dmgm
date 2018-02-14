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

import javafx.util.Pair;
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.impl.operators.fsm.common.DFSCode;
import org.biiig.dmgm.impl.operators.fsm.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.fsm.common.SubgraphMiningBase;
import org.biiig.dmgm.impl.operators.fsm.simple.FSMEmbedding;
import org.biiig.dmgm.impl.operators.fsm.simple.FSMGraph;
import org.biiig.dmgm.impl.operators.fsm.simple.FrequentSupportMethods;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class FrequentSimpleSubgraphs
  extends SubgraphMiningBase<FSMGraph, FSMEmbedding, Long> implements FrequentSupportMethods {

  /**
   * Constructor.
   *
   * @param db            database
   * @param parallel      flag to enable parallel execution
   * @param minSupportRel minimum support threshold
   * @param maxEdgeCount  maximum result edge count
   */
  public FrequentSimpleSubgraphs(PropertyGraphDB db, boolean parallel, float minSupportRel, int maxEdgeCount) {
    super(db, parallel, minSupportRel, maxEdgeCount);
  }

  @Override
  public Stream<FSMGraph> preProcess(Collection<CachedGraph> input) {
    return getParallelizableStream(input)
      .map(FSMGraph::new);
  }

  @Override
  public long[] output(
    List<Pair<DFSCode, Long>> frequentPatterns, Map<DFSCode, List<FSMEmbedding>> patternEmbeddings, Long minSupportAbsolute) {
    // use output of characteristic subgraphs
    return output(frequentPatterns);
  }

  @Override
  public BiFunction<FSMGraph, DFSEmbedding, FSMEmbedding> getEmbeddingFactory() {
    return (g, e) -> new FSMEmbedding(e);
  }
}
