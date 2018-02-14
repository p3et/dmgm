///*
// * This file is part of Directed Multigraph Miner (DMGM).
// *
// * DMGM is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * DMGM is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with DMGM. If not, see <http://www.gnu.org/licenses/>.
// */
//
///*
// * This file is part of Directed Multigraph Miner (DMGM).
// *
// * DMGM is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * DMGM is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with DMGM. If not, see <http://www.gnu.org/licenses/>.
// */
//
//package org.biiig.dmgm.impl.statistics;
//
//import com.google.common.collect.Maps;
//import org.biiig.dmgm.DMGMTestBase;
//import org.biiig.dmgm.TestConstants;
//import org.biiig.dmgm.api.db.PropertyGraphDB;
//import org.biiig.dmgm.impl.loader.TLFConstants;
//import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedCharacteristicSubgraphs;
//import org.junit.Test;
//
//import java.util.Map;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//public class VertexLabelSupportTest extends DMGMTestBase {
//
//  private static final Map<String, Long> EXPECTED_SUPPORT = Maps.newHashMap();
//
//  static {
//    EXPECTED_SUPPORT.put("S", 10L);
//    EXPECTED_SUPPORT.put("A", 10L);
//    EXPECTED_SUPPORT.put("B", 9L);
//    EXPECTED_SUPPORT.put("C", 8L);
//    EXPECTED_SUPPORT.put("D", 7L);
//    EXPECTED_SUPPORT.put("E", 6L);
//    EXPECTED_SUPPORT.put("F", 5L);
//    EXPECTED_SUPPORT.put("G", 4L);
//    EXPECTED_SUPPORT.put("H", 3L);
//    EXPECTED_SUPPORT.put("J", 2L);
//    EXPECTED_SUPPORT.put("K", 1L);
//  }
//
//  private final PropertyGraphDB db;
//  private final long cid;
//
//  public VertexLabelSupportTest() {
//    db = getPredictableDatabase();
//    int label = db.encode(TLFConstants.GRAPH_SYMBOL);
//    long[] graphIds = db.queryElements(i -> i == label);
//    this.cid = db.createCollection(label, graphIds);
//  }
//
//  @Test
//  public void getAbsolute() {
//    Map<Integer, Long> support = new GeneralizedCharacteristicSubgraphs(
//      db, TestConstants.PARALLEL, 0f, 0)
//      .getVertexLabelSupport(db.getCachedCollection(cid));
//
//    for (Map.Entry<Integer, Long> entry : support.entrySet()) {
//      String label = db.decode(entry.getKey());
//
//      assertTrue("did not expect " + entry, EXPECTED_SUPPORT.containsKey(label));
//      assertEquals("wrong support for " + label, EXPECTED_SUPPORT.get(label), entry.getValue());
//    }
//
//    for (Map.Entry<String, Long> entry : EXPECTED_SUPPORT.entrySet()) {
//      String label = entry.getKey();
//
//      assertTrue("did not find " + label, support.containsKey(db.encode(label)));
//    }
//  }
//}