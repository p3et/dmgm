
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

import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.impl.operators.common.WithDatabaseAccessBase;

/**
 * Get a pattern miner for graph collections.
 */
public class CollectionPatternMinerBuilder extends WithDatabaseAccessBase {

  /**
   * Constructor.
   *
   * @param database database
   * @param parallel true <=> parallel operator execution
   */
  CollectionPatternMinerBuilder(PropertyGraphDb database, boolean parallel) {
    super(database, parallel);
  }

  /**
   * Extract subgraph patterns which occur in at least a minimum number of graphs of a collection.
   *
   * @param minSupportThreshold patterns above this threshold will be considered as frequent
   * @param maxEdgeCount pattern above this size will not be extracted
   * @return frequent subgraph miner builder
   */
  public CollectionFrequentSubgraphsMinerBuilder extractFrequentSubgraphs(
      float minSupportThreshold, int maxEdgeCount) {

    return new CollectionFrequentSubgraphsMinerBuilder(
        database, parallel, minSupportThreshold, maxEdgeCount);
  }
}
