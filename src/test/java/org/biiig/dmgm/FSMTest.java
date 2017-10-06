package org.biiig.dmgm;

import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.api.model.graph.DMGraphFactory;
import org.biiig.dmgm.impl.model.collection.InMemoryGraphCollection;
import org.biiig.dmgm.impl.model.source.gdl.GDLDataSource;
import org.biiig.dmgm.impl.model.graph.SourceTargetMuxFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FSMTest {

  @Test
  public void testSingleEdge() {
    test(FSMTestData.SINGLE_EDGE_INPUT, FSMTestData.SINGLE_EDGE_EXPECTED);
  }


  @Test
  public void tedtSimpleGraph() {
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
    new GDLDataSource(inputGDL).load(expectedDB, graphFactory);

    DirectedMultigraphMiner.frequentSubgraphs(inputDB, outputDB, 0.6f);

    assertEquals("graph count", inputDB.getGraphCount(), outputDB.getGraphCount());
  }

}
