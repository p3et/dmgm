package org.biiig.dmgm.impl.algorithms.subgraph_mining.common;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.Operator;
import org.junit.Test;

public abstract class SubgraphMiningPatternTest extends DMGMTestBase {
  @Test
  public void testSingleEdge() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.SINGLE_EDGE_INPUT, SubgraphMiningTestData.SINGLE_EDGE_EXPECTED);
  }

  @Test
  public void testSimpleGraph() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.SIMPLE_GRAPH_INPUT, SubgraphMiningTestData.SIMPLE_GRAPH_EXPECTED);
  }

  @Test
  public void testParallelEdges() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.PARALLEL_EDGES_INPUT, SubgraphMiningTestData.PARALLEL_EDGES_EXPECTED);
  }

  @Test
  public void testLoop() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.LOOP_INPUT, SubgraphMiningTestData.LOOP_EXPECTED);
  }

  @Test
  public void testDiamond() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.DIAMOND_INPUT, SubgraphMiningTestData.DIAMOND_EXPECTED);
  }

  @Test
  public void testCircleWithBranch() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.CIRCLE_WITH_BRANCH_INPUT, SubgraphMiningTestData.CIRCLE_WITH_BRANCH_EXPECTED);
  }

  @Test
  public void testMultiLabeledCircle() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.MULTI_LABELED_CIRCLE_INPUT, SubgraphMiningTestData.MULTI_LABELED_CIRCLE_EXPECTED);
  }

  protected abstract Operator getOperator();

}