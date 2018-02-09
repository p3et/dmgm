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

package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.PropertyGraphDB;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public abstract class SubgraphMiningThresholdTest extends DMGMTestBase {

  protected abstract CollectionOperator getOperator(PropertyGraphDB db, float minSupportRel, int maxEdgeCount);

  @Test
  public void mine10() throws Exception {
    mine(1.0f, 702);
  }

  @Test
  public void mine08() throws Exception {
    mine(0.8f, 2106);
  }

  private void mine(float minSupportThreshold, int expectedResultSize) throws IOException {
    PropertyGraphDB db = getPredictableDatabase();

    CollectionOperator fsm = getOperator(db, minSupportThreshold, 20);

    long inId = db.createCollection(0, db.getGraphIds());

    fsm.sequential();

    Long outId = fsm.apply(inId);
    long[] graphIds = db.getGraphElementIds(outId).getVertexIds();
    assertEquals("sequential @ " + minSupportThreshold,expectedResultSize, graphIds.length);

    fsm.parallel();

    outId = fsm.apply(inId);
    graphIds = db.getGraphElementIds(outId).getVertexIds();
    assertEquals("parallel @ " + minSupportThreshold, expectedResultSize, graphIds.length);
  }

}