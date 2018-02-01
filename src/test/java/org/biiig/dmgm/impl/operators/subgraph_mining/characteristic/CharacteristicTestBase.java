package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.GraphDB;
import org.junit.Test;

import java.util.function.Function;

public abstract class CharacteristicTestBase extends DMGMTestBase {
  @Test
  public void testAlgorithm() {
    String gdl =
      ":IN{_category:\"X\"}[(:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":IN{_category:\"X\"}[(:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":IN{_category:\"Y\"}[(:A)-[:a]->(:B)-[:b]->(:C)-[:b]->(:D)]" +
      ":IN{_category:\"Y\"}[(:A)-[:a]->(:B)-[:b]->(:C)-[:b]->(:E)]" +
      ":EX[(:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":EX[(:A)-[:a]->(:B)-[:a]->(:C)]" +
      ":EX[(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":EX[(:B)-[:a]->(:C)]" +
      ":EX[(:C)-[:a]->(:D)]" +
      ":EX[(:A)-[:a]->(:B)]" +
      ":EX[(:A)-[:a]->(:B)-[:b]->(:C)]" +
      ":EX[(:B)-[:b]->(:C)]" ;

    runAndTestExpectation(getOperator(), gdl, false);
  }

  public abstract Function<GraphDB, CollectionOperator> getOperator();
}
