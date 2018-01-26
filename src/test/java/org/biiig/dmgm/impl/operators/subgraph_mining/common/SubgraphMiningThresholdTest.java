//package org.biiig.dmgm.impl.operators.subgraph_mining.common;
//
//import org.biiig.dmgm.api.HyperVertexOperator;
//import org.biiig.dmgm.DMGMTestBase;
//import org.junit.Test;
//
//import java.io.IOException;
//
//import static junit.framework.TestCase.assertEquals;
//
//public abstract class SubgraphMiningThresholdTest extends DMGMTestBase {
//
//  protected abstract HyperVertexOperator getOperator(float minSupportRel, int maxEdgeCount);
//
//  @Test
//  public void mine10() throws Exception {
//    mine(1.0f, 702);
//  }
//
//  @Test
//  public void mine08() throws Exception {
//    mine(0.8f, 2106);
//  }
//
//  private void mine(float minSupportThreshold, int expectedResultSize) throws IOException {
//    HyperVertexOperator fsm = getOperator(minSupportThreshold, 20);
//    GraphCollection input = getPredictableDatabase();
//    GraphCollection output = input.apply(fsm);
//    assertEquals(expectedResultSize, output.size());
//  }
//
//}