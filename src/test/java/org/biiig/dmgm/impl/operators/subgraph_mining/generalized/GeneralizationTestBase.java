package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.Operator;
import org.junit.Test;

public abstract class GeneralizationTestBase extends DMGMTestBase {
  @Test
  public void testAlgorithm() {
    Operator operator = getOperator();

    String inputGDL = (
      ":X[(:A_a_a)-[:a]->(:B_b_b)-[:a]->(:C)]" +
      ":X[(:A_a_b)-[:a]->(:B_b_b_b)-[:a]->(:C)]" );

    String expectedGDL = (
      ":X[(:A_a)-[:a]->(:B_b)-[:a]->(:C)]" +
      ":X[(:A_a)-[:a]->(:B_b_b)-[:a]->(:C)]" +
      ":X[(:A_a)-[:a]->(:B_b)]" +
      ":X[(:A_a)-[:a]->(:B_b_b)]" +
      ":X[(:B_b)-[:a]->(:C)]" +
      ":X[(:B_b_b)-[:a]->(:C)]"
    );

    runAndTestExpectation(operator, inputGDL, expectedGDL);
  }

  public abstract Operator getOperator();
}
