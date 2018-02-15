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

import org.biiig.dmgm.api.config.DMGMConstants;
import org.biiig.dmgm.api.db.PropertyGraphDB;

import java.util.Map;

/**
 * Superclass of characteristic subgraph mining variants.
 *
 * @param <G> input graph type
 */
public abstract class CharacteristicSubgraphsBase<G extends WithGraph & WithCategories>
  extends SubgraphMiningBase<G, Map<Integer, Long>> implements CharacteristicSupportMethods<G> {

  /**
   * Get the category property key.
   */
  private final int categoryKey;
  /**
   * Get the default category property value.
   */
  private final int[] defaultCategories;

  CharacteristicSubgraphsBase(PropertyGraphDB db, boolean parallel, float minSupportRel, int maxEdgeCount) {
    super(db, parallel, minSupportRel, maxEdgeCount);
    categoryKey = db.encode(DMGMConstants.PropertyKeys.CATEGORY);
    defaultCategories = new int[] {db.encode(DMGMConstants.PropertyDefaultValues.STRING)};
  }

  @Override
  public int getCategoryKey() {
    return categoryKey;
  }

  @Override
  public int[] getDefaultCategories() {
    return defaultCategories;
  }
}
