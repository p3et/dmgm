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

import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.api.operators.CollectionToCollectionOperator;

/**
 * Factory for frequent subgraph miners.
 */
public class FrequentSubgraphMiningFactory {

  /**
   * Database.
   */
  private final PropertyGraphDb db;

  /**
   * Constructor.
   *
   * @param db database in which the operations should be executed
   */
  public FrequentSubgraphMiningFactory(PropertyGraphDb db) {
    this.db = db;
  }

  /**
   * Create a frequent subgraph mining operator.
   *
   * @param minSupportRel min support threshold
   * @param maxEdgeCount max edge count of extracted patterns
   * @param parallel true <=> enable parallel execution
   * @param generalized true <=> enable mining of generalized patterns :
   *                    Vertex labels containing
   *                    {@code DmgmConstants.Separators.TAXONOMY_PATH_LEVEL}
   *                    will be used to form taxonomies and patterns for all frequent combinations
   *                    of on all taxonomy levels will be extracted
   * @param characteristic true <=> enable mining of characteristic patterns :
   *                       Graphs will be categorized and a pattern will be extracted
   *                       for all categories if it is frequent in at least one category.
   *                       Categories are associated by graph properties of {@code String} or
   *                       {@code String[]} with key {@code DmgmConstants.PropertyKeys.CATEGORY}.
   */
  CollectionToCollectionOperator create(
      float minSupportRel, int maxEdgeCount,
      boolean parallel, boolean generalized, boolean characteristic) {

    CollectionToCollectionOperator operator;
    if (generalized && characteristic) {
      operator = new CharacteristicGeneralizedSubgraphs(db, parallel, minSupportRel, maxEdgeCount);
    } else  if (generalized) {
      operator = new FrequentGeneralizedSubgraphs(db, parallel, minSupportRel, maxEdgeCount);
    } else  if (characteristic) {
      operator = new CharacteristicSimpleSubgraphs(db, parallel, minSupportRel, maxEdgeCount);
    } else {
      operator = new FrequentSimpleSubgraphs(db, parallel, minSupportRel, maxEdgeCount);
    }

    return operator;
  }
}
