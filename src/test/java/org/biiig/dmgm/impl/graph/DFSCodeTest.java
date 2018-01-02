package org.biiig.dmgm.impl.graph;

import org.biiig.dmgm.api.GraphFactory;

import static org.junit.Assert.assertEquals;

public class DFSCodeTest extends SingleLabelDirectedGraphTest {

  @Override
  GraphFactory getFactory() {
    return new DFSCodeFactory();
  }

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