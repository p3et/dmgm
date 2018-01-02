package org.biiig.dmgm.impl.algorithms.tfsm;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.impl.graph_loader.gdl.GDLLoader;
import org.biiig.dmgm.DMGMTestBase;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FSMTest extends DMGMTestBase {

  @Test
  public void testSingleEdge() {
    test(FSMTestData.SINGLE_EDGE_INPUT, FSMTestData.SINGLE_EDGE_EXPECTED);
  }


  @Test
  public void testSimpleGraph() {
    test(FSMTestData.SIMPLE_GRAPH_INPUT, FSMTestData.SIMPLE_GRAPH_EXPECTED);
  }

  @Test
  public void testParallelEdges() {
    test(FSMTestData.PARALLEL_EDGES_INPUT, FSMTestData.PARALLEL_EDGES_EXPECTED);
  }

  @Test
  public void testLoop() {
    test(FSMTestData.LOOP_INPUT, FSMTestData.LOOP_EXPECTED);
  }

  @Test
  public void testDiamond() {
    test(FSMTestData.DIAMOND_INPUT, FSMTestData.DIAMOND_EXPECTED);
  }

  @Test
  public void testCircleWithBranch() {
    test(FSMTestData.CIRCLE_WITH_BRANCH_INPUT, FSMTestData.CIRCLE_WITH_BRANCH_EXPECTED);
  }

  @Test
  public void testMultiLabeledCircle() {
    test(FSMTestData.MULTI_LABELED_CIRCLE_INPUT, FSMTestData.MULTI_LABELED_CIRCLE_EXPECTED);
  }

  private void test(String inputGDL, String expectedGDL) {
    GraphCollection input = GDLLoader
      .fromString(inputGDL)
      .getGraphCollection();

    GraphCollection expected = GDLLoader
      .fromString(expectedGDL)
      .getGraphCollection();

    GraphCollection output = new FrequentSubgraphs(0.6f, 10)
      .apply(input);

    assertTrue("constistent", isConsistent(output));
    assertTrue("equals", equal(expected, output));
  }

  private boolean isConsistent(GraphCollection collection) {
    boolean consistent = true;

    for (Graph graph : collection) {
      for (int vertexId = 0; vertexId < graph.getVertexCount(); vertexId++ ) {
        int vertexLabel = graph.getVertexLabel(vertexId);
        String translation = collection.getVertexDictionary().translate(vertexLabel);

        consistent = consistent && translation != null;

        if (translation == null) {
          System.out.println("no translation found for integer vertex label " + vertexLabel);
        }
      }

      for (int edgeId = 0; edgeId < graph.getEdgeCount(); edgeId++ ) {
        int edgeLabel = graph.getEdgeLabel(edgeId);
        String translation = collection.getEdgeDictionary().translate(edgeLabel);

        consistent = consistent && translation != null;

        if (translation == null) {
          System.out.println("no translation found for integer edge label " + edgeLabel);
        }
      }
    }

    return consistent;
  }

}
