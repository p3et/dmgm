package org.biiig.dmgm.impl.graph;

import org.biiig.dmgm.api.SmallGraph;
import org.junit.Test;

public class DFSCodeTest extends SingleLabelDirectedSmallGraphTest {

  private static final String PATH_CHILD = "[(:V)-[:e]->(:V)-[:e]->(:V)]";
  private static final String CYCLE_CHILD = "[(v:V)-[:e]->(:V)-[:e]->(v)]";
  private static final String EXPECTED_PARENT = "[(:V)-[:e]->(:V)]";

  @Test
  public void testGetterAndSetter() throws Exception {
    int lab0 = 0;
    int lab1 = 1;

    SmallGraph graph = new DFSCode(
      new int[] {lab0, lab1},
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
//      .create()
//      .withLabelDictionary(input.getLabelDictionary())
//      .create();
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