package org.biiig.dmgm.impl.algorithms.fsm.ccp;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.Operator;
import org.junit.Test;

public class CCPAlgorithmTest extends DMGMTestBase {

  @Test
  public void testAlgorithm() {

    Operator operator =
      new CategoryCharacteristicSubgraphs(0.5f, 10, g -> String.valueOf(g.getLabel()), (c, t) -> c >= t/2);

    String input =
      ":X[(:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":X[(:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":Y[(:A)-[:a]->(:B)-[:b]->(:C)-[:b]->(:D)]" +
      ":Y[(:A)-[:a]->(:B)-[:b]->(:C)-[:b]->(:E)]";

    String expectation =
      "[(:B)-[:a]->(:C)-[:a]->(:D)]" +
      "[(:B)-[:a]->(:C)]" +
      "[(:C)-[:a]->(:D)]" +
      "[(:B)-[:b]->(:C)]";

    runAndTestExpectation(operator, input, expectation);
  }
}
