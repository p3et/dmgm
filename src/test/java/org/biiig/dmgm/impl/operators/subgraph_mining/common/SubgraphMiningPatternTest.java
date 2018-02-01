package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.GraphDB;
import org.junit.Test;

import java.util.function.Function;

public abstract class SubgraphMiningPatternTest extends DMGMTestBase {
  @Test
  public void testSingleEdge() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.SINGLE_EDGE, false);
  }

  @Test
  public void testSimpleGraph() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.SIMPLE_GRAPH_INPUT, false);
  }

  @Test
  public void testY() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.Y, false);
  }

  @Test
  public void testParallelEdges() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.PARALLEL_EDGES_INPUT, false);
  }

  @Test
  public void testLoop() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.LOOP_INPUT, false);
  }

  @Test
  public void testDiamond() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.DIAMOND_INPUT, false);
  }

  @Test
  public void testCircleWithBranch() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.CIRCLE_WITH_BRANCH_INPUT, false);
  }

  @Test
  public void testMultiLabeledCircle() {
    runAndTestExpectation(getOperator(), SubgraphMiningTestData.MULTI_LABELED_CIRCLE_INPUT, false);
  }

  protected abstract Function<GraphDB, CollectionOperator> getOperator();

}
