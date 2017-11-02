package org.biiig.dmgm;

import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.api.model.graph.DMGraphFactory;
import org.biiig.dmgm.impl.algorithms.tfsm.TFSMConfig;
import org.biiig.dmgm.impl.model.collection.InMemoryGraphCollection;
import org.biiig.dmgm.impl.model.graph.SourceTargetMuxFactory;
import org.biiig.dmgm.impl.model.source.gdl.GDLDataSource;
import org.biiig.dmgm.io.DMGMTestBase;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FSMTest extends DMGMTestBase {

  private static final TFSMConfig TFSM_CONFIG = new TFSMConfig(0.6f, 10);

  @Test
  public void testSingleEdge() {
    test(FSMTestData.SINGLE_EDGE_INPUT, FSMTestData.SINGLE_EDGE_EXPECTED);
  }


  @Test
  public void testSimpleGraph() {
    test(FSMTestData.SIMPLE_GRAPH_INPUT,FSMTestData.SIMPLE_GRAPH_EXPECTED);
  }

  @Test
  public void testParallelEdges() {
    test(FSMTestData.PARALLEL_EDGES_INPUT,FSMTestData.PARALLEL_EDGES_EXPECTED);
  }

  @Test
  public void testLoop() {
    test(FSMTestData.LOOP_INPUT,FSMTestData.LOOP_EXPECTED);
  }

  @Test
  public void testDiamond() {
    test(FSMTestData.DIAMOND_INPUT,FSMTestData.DIAMOND_EXPECTED);
  }

  @Test
  public void testCircleWithBranch() {
    test(FSMTestData.CIRCLE_WITH_BRANCH_INPUT, FSMTestData.CIRCLE_WITH_BRANCH_EXPECTED);
  }

  @Test
  public void testMultiLabeledCircle() {
    test(FSMTestData.MULTI_LABELED_CIRCLE_INPUT,FSMTestData.MULTI_LABELED_CIRCLE_EXPECTED);
  }

  private void test(String inputGDL, String expectedGDL) {
    DMGraphFactory graphFactory = new SourceTargetMuxFactory();

    DMGraphCollection inputDB = new InMemoryGraphCollection();
    DMGraphCollection expectedDB = new InMemoryGraphCollection();
    DMGraphCollection outputDB = new InMemoryGraphCollection();

    new GDLDataSource(inputGDL).load(inputDB, graphFactory);
    new GDLDataSource(expectedGDL).load(expectedDB, graphFactory);

    DirectedMultigraphMiner.frequentSubgraphs(inputDB, outputDB, TFSM_CONFIG);

    assertTrue("constistent", isConsistent(outputDB));
    assertTrue("equals", equal(expectedDB, outputDB));
  }

  private boolean isConsistent(DMGraphCollection collection) {
    boolean consistent = true;

    for (DMGraph graph : collection) {
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
