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

package org.biiig.dmgm.impl.statistics;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.TestConstants;
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.impl.loader.TLFConstants;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedCharacteristicSubgraphs;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class VertexLabelSupportTest extends DMGMTestBase {

  private final PropertyGraphDB db;
  private final long cid;

  public VertexLabelSupportTest() throws IOException {
    db = getPredictableDatabase();
    int label = db.encode(TLFConstants.GRAPH_SYMBOL);
    long[] graphIds = db.queryElements(i -> i == label);
    this.cid = db.createCollection(label, graphIds);
  }

  @Test
  public void getAbsolute() throws IOException {
    Map<Integer, Long> support = new GeneralizedCharacteristicSubgraphs(db, TestConstants.PARALLEL, 0f, 0)
      .getVertexLabelSupport(db.getCachedCollection(cid));

    for (int i = 1; i <= 10; i++)
      assertTrue("cannot find support of 1", support.values().contains(i));
  }
}