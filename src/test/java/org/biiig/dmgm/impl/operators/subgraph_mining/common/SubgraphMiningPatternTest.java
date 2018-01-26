package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.HyperVertexOperator;
import org.junit.Test;

public abstract class SubgraphMiningPatternTest extends DMGMTestBase {
  @Test
  public void testSingleEdge() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.SINGLE_EDGE);
  }

  @Test
  public void testSimpleGraph() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.SIMPLE_GRAPH_INPUT);
  }

  @Test
  public void testY() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.Y);
  }

  @Test
  public void testParallelEdges() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.PARALLEL_EDGES_INPUT);
  }

  @Test
  public void testLoop() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.LOOP_INPUT);
  }

  @Test
  public void testDiamond() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.DIAMOND_INPUT);
  }

  @Test
  public void testCircleWithBranch() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.CIRCLE_WITH_BRANCH_INPUT);
  }

  @Test
  public void testMultiLabeledCircle() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.MULTI_LABELED_CIRCLE_INPUT);
  }

  protected abstract HyperVertexOperator getOperator();

}
