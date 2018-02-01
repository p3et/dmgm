package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.GraphDB;
import org.junit.Test;

import java.util.function.Function;

public abstract class GeneralizationTestBase extends DMGMTestBase {
  @Test
  public void testAlgorithm() {
    Function<GraphDB, CollectionOperator> operator = getOperator();

    String inputGDL =
      ":IN[(:A_a_a)-[:a]->(:B_b_b)-[:a]->(:C)]" +
      ":IN[(:A_a_b)-[:a]->(:B_b_b_b)-[:a]->(:C)]" +
      ":EX[(:A_a)-[:a]->(:B_b)-[:a]->(:C)]" +
      ":EX[(:A_a)-[:a]->(:B_b_b)-[:a]->(:C)]" +
      ":EX[(:A_a)-[:a]->(:B_b)]" +
      ":EX[(:A_a)-[:a]->(:B_b_b)]" +
      ":EX[(:B_b)-[:a]->(:C)]" +
      ":EX[(:B_b_b)-[:a]->(:C)]";

    runAndTestExpectation(operator, inputGDL, false);
  }

  public abstract Function<GraphDB, CollectionOperator> getOperator();
}
