package org.biiig.dmgm.impl.operators.subgraph_mining.gcsm;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedCharacteristicSubgraphs;
import org.junit.Test;

public class GCSMCharacteristicTest extends DMGMTestBase {

  @Test
  public void testAlgorithm() {
    CollectionOperator operator = new GeneralizedCharacteristicSubgraphs(
      1.0f,
      10,
      (c, t) -> c.size() == 1 ? ArrayUtils.toPrimitive(c.keySet().toArray(new Integer[1])) : null
    );

    String gdl =
      ":IN{_category:\"X\"}[(:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":IN{_category:\"X\"}[(:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":IN{_category:\"Y\"}[(:A)-[:a]->(:B)-[:b]->(:C)-[:b]->(:D)]" +
      ":IN{_category:\"Y\"}[(:A)-[:a]->(:B)-[:b]->(:C)-[:b]->(:E)]" +
      ":EX{_category:\"X\"}[(:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":EX{_category:\"X\"}[(:A)-[:a]->(:B)-[:a]->(:C)]" +
      ":EX{_category:\"X\"}[(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":EX{_category:\"X\"}[(:B)-[:a]->(:C)]" +
      ":EX{_category:\"X\"}[(:A)-[:a]->(:B)]" +
      ":EX{_category:\"X\"}[(:A)-[:a]->(:B)-[:b]->(:C)]" +
      ":EX{_category:\"X\"}[(:B)-[:b]->(:C)]" ;

    runAndTestExpectation(operator, gdl);
  }
}
