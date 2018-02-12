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

package org.biiig.dmgm.impl.model;

import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.impl.operators.subgraph_mining.DFSCode;
import org.junit.Test;

public class DFSCodeTest extends SingleLabelDirectedSmallGraphTest {

  private static final String PATH_CHILD = "[(:V)-[:e]->(:V)-[:e]->(:V)]";
  private static final String CYCLE_CHILD = "[(v:V)-[:e]->(:V)-[:e]->(v)]";
  private static final String EXPECTED_PARENT = "[(:V)-[:e]->(:V)]";

  @Test
  public void testGetterAndSetter() throws Exception {
    int lab0 = 0;
    int lab1 = 1;

    CachedGraph graph = new DFSCode(
      0, new int[] {lab0, lab1},
      new int[] {lab0, lab1},
      new int[] {0, 0},
      new int[] {0, 1},
      new boolean[] {true, true}
    );

    test(graph, lab0, lab1);
  }

//  @Test
//  public void getParentOfPath() {
//    getParentOf(PATH_CHILD);
//  }
//
//  @Test
//  public void getParentOfCycle() {
//    getParentOf(CYCLE_CHILD);
//  }
//
//  public void getParentOf(String child) {
//    GraphCollection input = GDLLoader
//      .fromString(child)
//      .withGraphFactory(getFactory())
//      .getGraphCollection();
//
//    GraphCollection output = new InMemoryGraphCollectionBuilderFactory()
//      .get()
//      .withLabelDictionary(input.getLabelDictionary())
//      .get();
//
//    input.forEach(g -> output.add(((DFSCode) g).getParent()));
//
//    GraphCollection expected = GDLLoader
//      .fromString(EXPECTED_PARENT)
//      .withGraphFactory(getFactory())
//      .getGraphCollection();
//
//    assertTrue(equal(expected, output, db));
//  }

//  @Test
//  public void testForwardsGrowth() {
//    DFSCode parent =
//      new DFSCode(0, 1, 0, true, 0, 0);
//
//    DFSCode child = parent.growChild(1, 2, true, 0, 0);
//
//    assertEquals("forwards vertex count", 3, child.getVertexCount());
//    assertEquals("forwards edge count", 2, child.getEdgeCount());
//  }
//
//  @Test
//  public void testBackwardsGrowth() {
//    DFSCode parent =
//      new DFSCode(0, 1, 0, true, 0, 0);
//
//    DFSCode child = parent.growChild(1, 0, true, 0, 0);
//
//    assertEquals("forwards vertex count", 2, child.getVertexCount());
//    assertEquals("forwards edge count", 2, child.getEdgeCount());
//  }
//
//  @Test
//  public void testGetter() {
//    DFSCode parent =
//      new DFSCode(0, 1, 33, true, 44, 55);
//
//    DFSCode child = parent.growChild(1, 0, true, 66, 77);
//
//    assertEquals(child.getVertexLabel(child.getToTime(0)), 55);
//    assertEquals(child.getVertexLabel(child.getToTime(1)), 33);
//  }
}