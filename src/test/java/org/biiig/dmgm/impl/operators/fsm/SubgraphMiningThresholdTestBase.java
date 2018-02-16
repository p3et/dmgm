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

import org.biiig.dmgm.DmgmTestBase;
import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.api.operators.CollectionToCollectionOperator;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public abstract class SubgraphMiningThresholdTestBase extends DmgmTestBase {

  protected abstract CollectionToCollectionOperator getOperator(
      PropertyGraphDb db, boolean b, float minSupportRel, int maxEdgeCount);

  @Test
  public void mine10() {
    mine(1.0f, 702);
  }

  @Test
  public void mine08() {
    mine(0.8f, 2106);
  }

  private void mine(float minSupportThreshold, int expectedResultSize) {
    PropertyGraphDb db = getPredictableDatabase();

    CollectionToCollectionOperator fsm =
        getOperator(db, false, minSupportThreshold, 20);

    long inId = db.createCollection(0, db.getGraphIds());

    Long outId = fsm.apply(inId);
    long[] graphIds = db.getGraphIdsOfCollection(outId);
    assertEquals(
        "sequential @ " + minSupportThreshold,expectedResultSize, graphIds.length);

    fsm = getOperator(db, true, minSupportThreshold, 20);

    outId = fsm.apply(inId);
    graphIds = db.getGraphIdsOfCollection(outId);
    assertEquals(
        "parallel @ " + minSupportThreshold, expectedResultSize, graphIds.length);
  }

}