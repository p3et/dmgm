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

package org.biiig.dmgm.impl.operators.patternmining;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javafx.util.Pair;
import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.api.model.CachedGraph;

/**
 * Simple frequent subgraph mining. This is the multi-threaded version of DIMSpan.
 *
 * @see <a href="https://dl.acm.org/citation.cfm?id=3148064">Paper</a>
 */
class FrequentSimpleSubgraphs
    extends SubgraphMiningBase<CachedGraph, Long> implements FrequentSupport<CachedGraph> {

  /**
   * Constructor.
   *
   * @param db            database
   * @param parallel      flag to enable parallel execution
   * @param minSupportRel minimum support threshold
   * @param maxEdgeCount  maximum result edge count
   */
  FrequentSimpleSubgraphs(
      PropertyGraphDb db, boolean parallel, float minSupportRel, int maxEdgeCount) {

    super(db, parallel, minSupportRel, maxEdgeCount);
  }

  @Override
  public Stream<CachedGraph> preProcess(Collection<CachedGraph> input) {
    // just forward the input
    return getParallelizableStream(input);
  }

  @Override
  public long[] output(List<Pair<DfsCode, Long>> frequentPatterns,
                       Map<DfsCode, List<WithEmbedding>> patternEmbeddings,
                       Map<Long, CachedGraph> graphIndex, Long minSupportAbsolute) {

    // just output a single pattern
    return output(frequentPatterns);
  }

}
