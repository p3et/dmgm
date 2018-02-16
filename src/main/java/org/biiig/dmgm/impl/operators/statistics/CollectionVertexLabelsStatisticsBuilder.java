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

package org.biiig.dmgm.impl.operators.statistics;

import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.impl.operators.common.WithDatabaseAccessBase;

public class CollectionVertexLabelsStatisticsBuilder extends WithDatabaseAccessBase {

  /**
   * Constructor.
   *
   * @param database database
   * @param parallel true <=> parallel operator execution
   */
  CollectionVertexLabelsStatisticsBuilder(PropertyGraphDb database, boolean parallel) {
    super(database, parallel);
  }

  /**
   * Return extractor for vertex label support.
   *
   * @param generalized true <=> include generalized labels
   *
   * @return extractor
   */
  public CollectionVertexLabelSupport getSupport(boolean generalized) {
    return new CollectionVertexLabelSupport(database, parallel, generalized);
  }

}
