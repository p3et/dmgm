package org.biiig.dmgm;

import org.junit.Test;

public class FSMTest {

  @Test
  public void testSingleEdge() {
    test(FSMTestData.FSM_SINGLE_EDGE, "g1,g2,g3,g4", "s1");
  }


  @Test
  public void tedtSimpleGraph() {
    test(FSMTestData.FSM_SIMPLE_GRAPH,"g1,g2,g3","s1,s2,s3,s4,s5");
  }

  @Test
  public void testParallelEdges() {
    test(FSMTestData.FSM_PARALLEL_EDGES,"g1,g2,g3","s1,s2");
  }

  @Test
  public void testLoop() {
    test(FSMTestData.FSM_LOOP,"g1,g2,g3,g4","s1,s2,s3");
  }

  @Test
  public void testDiamond() {
    test(FSMTestData.FSM_DIAMOND,"g1,g2,g3","s1,s2,s3,s4,s5,s6,s7");
  }

  @Test
  public void testCircleWithBranch() {
    test(FSMTestData.FSM_CIRCLE_WITH_BRANCH,
      "g1,g2,g3","s1,s2,s3,s4,s5,s6,s7,s8,s9,s10");
  }

  @Test
  public void testMultiLabeledCircle() {
    test(FSMTestData.MULTI_LABELED_CIRCLE,"g1,g2","s1,s2,s3,s4,s5,s6,s7");
  }

  private void test(String data, String inputGraphs, String outputGraphs) {
    System.out.println("Hello!");


  }

}
