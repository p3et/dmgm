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
import org.biiig.dmgm.api.operators.CollectionToCollectionOperator;

/**
 * Get a frequent subgraph miner.
 */
public class CollectionFrequentSubgraphsMinerBuilder extends CollectionSubgraphMinerBuilder {

  /**
   * Constructor.
   *  @param database database
   * @param parallel true <=> parallel operator execution
   * @param minSupportThreshold relative min support
   * @param maxEdgeCount max edge count
   */
  CollectionFrequentSubgraphsMinerBuilder(
      PropertyGraphDb database, boolean parallel, float minSupportThreshold, int maxEdgeCount) {

    super(database, parallel, maxEdgeCount, minSupportThreshold);
  }

  /**
   * Get a basic frequent subgraph miner.
   *
   * @return miner
   */
  public CollectionToCollectionOperator get() {
    return new FrequentSimpleSubgraphs(
        database, parallel, minSupportThreshold, maxEdgeCount);
  }

  /**
   * Get a generalized frequent subgraph miner:
   * Vertex labels containing {@code DmgmConstants.Separators.TAXONOMY_PATH_LEVEL}
   * will be used to form taxonomies and patterns for all frequent combinations
   * of labels on all taxonomy levels will be extracted.
   *
   * @see <a href="http://ieeexplore.ieee.org/document/8244685/">Generalized Frequent Subgraph Mining</a>
   *
   * @return miner
   */
  public CollectionToCollectionOperator getGeneralized() {
    return new FrequentGeneralizedSubgraphs(
        database, parallel, minSupportThreshold, maxEdgeCount);
  }

  /**
   * Get a basic characteristic subgraph miner:
   * Graphs will be categorized and a pattern will be extracted for all categories
   * if it is frequent in at least one category. Categories are associated by graph properties
   * of {@code String} or {@code String[]} with key {@code DmgmConstants.PropertyKeys.CATEGORY}.
   *
   * @see <a href="https://www.degruyter.com/view/j/itit.2016.58.issue-4/itit-2016-0006/itit-2016-0006.xml">Characteristic Subgraph Mining</a>
   *
   * @return miner
   */
  public CollectionToCollectionOperator getCharacteristic() {
    return new CharacteristicSimpleSubgraphs(
        database, parallel, minSupportThreshold, maxEdgeCount);
  }

  /**
   * Get a generalized characteristic subgraph miner:
   * Vertex labels containing {@code DmgmConstants.Separators.TAXONOMY_PATH_LEVEL}
   * will be used to form taxonomies and patterns for all frequent combinations
   * of labels on all taxonomy levels will be extracted.
   * Graphs will be categorized and a pattern will be extracted for all categories
   * if it is frequent in at least one category. Categories are associated by graph properties
   * of {@code String} or {@code String[]} with key {@code DmgmConstants.PropertyKeys.CATEGORY}.
   *
   * @see <a href="http://ieeexplore.ieee.org/document/8244685/">Generalized Frequent Subgraph Mining</a>
   * @see <a href="https://www.degruyter.com/view/j/itit.2016.58.issue-4/itit-2016-0006/itit-2016-0006.xml">Characteristic Subgraph Mining</a>
   *
   * @return miner
   */
  public CollectionToCollectionOperator getGeneralizedCharacteristic() {
    return new CharacteristicGeneralizedSubgraphs(
        database, parallel, minSupportThreshold, maxEdgeCount);
  }

}
