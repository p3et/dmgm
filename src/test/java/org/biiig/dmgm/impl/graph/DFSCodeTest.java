package org.biiig.dmgm.impl.graph;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.graph_collection.InMemoryGraphCollectionBuilderFactory;
import org.biiig.dmgm.impl.graph_loader.gdl.GDLLoader;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DFSCodeTest extends SingleLabelDirectedSmallGraphTest {

  private static final String PATH_CHILD = "[(:V)-[:e]->(:V)-[:e]->(:V)]";
  private static final String CYCLE_CHILD = "[(v:V)-[:e]->(:V)-[:e]->(v)]";
  private static final String EXPECTED_PARENT = "[(:V)-[:e]->(:V)]";


  @Override
  GraphFactory getFactory() {
    return new DFSCodeFactory();
  }

  @Test
  public void getParentOfPath() {
    getParentOf(PATH_CHILD);
  }

  @Test
  public void getParentOfCycle() {
    getParentOf(CYCLE_CHILD);
  }

  public void getParentOf(String child) {
    GraphCollection input = GDLLoader
      .fromString(child)
      .withGraphFactory(getFactory())
      .getGraphCollection();

    GraphCollection output = new InMemoryGraphCollectionBuilderFactory()
      .create()
      .withLabelDictionary(input.getLabelDictionary())
      .create();

    input.forEach(g -> output.add(((DFSCode) g).getParent()));

    GraphCollection expected = GDLLoader
      .fromString(EXPECTED_PARENT)
      .withGraphFactory(getFactory())
      .getGraphCollection();

    assertTrue(equal(expected, output));
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