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
      ":IN[(:A_A)-[:a]->(:B_B)-[:a]->(:C)]" +
      ":IN[(:A_B)-[:a]->(:B_B_B)-[:a]->(:C)]" +
      ":EX[(:A)-[:a]->(:B)]" +
      ":EX[(:A)-[:a]->(:B_B)]" +
      ":EX[(:B)-[:a]->(:C)]" +
      ":EX[(:B_B)-[:a]->(:C)]" +
      ":EX[(:A)-[:a]->(:B_B)-[:a]->(:C)]" +
      ":EX[(:A)-[:a]->(:B)-[:a]->(:C)]";

    runAndTestExpectation(operator, inputGDL, false);
  }

  public abstract Function<GraphDB, CollectionOperator> getOperator();
}
