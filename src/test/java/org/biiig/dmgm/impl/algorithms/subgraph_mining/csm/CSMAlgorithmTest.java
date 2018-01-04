package org.biiig.dmgm.impl.algorithms.subgraph_mining.csm;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPropertyKeys;
import org.biiig.dmgm.impl.graph_loader.gdl.GDLLoader;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class CSMAlgorithmTest extends DMGMTestBase {

  @Test
  public void testAlgorithm() {

    Operator operator = new CharacteristicSubgraphs(
      0.5f, 10, g -> String.valueOf(g.getLabel()), (c, t) -> c > t/2);

    String inputGDL =
      ":X[(:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":X[(:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":Y[(:A)-[:a]->(:B)-[:b]->(:C)-[:b]->(:D)]" +
      ":Y[(:A)-[:a]->(:B)-[:b]->(:C)-[:b]->(:E)]";

    String expectation =
      "[(:B)-[:a]->(:C)-[:a]->(:D)]" +
        "[(:B)-[:a]->(:C)]" +
        "[(:C)-[:a]->(:D)]" +
        "[(:B)-[:b]->(:C)]";

    GraphCollection input = GDLLoader
      .fromString(inputGDL)
      .getGraphCollection();

    GraphCollection output = input.apply(operator);

    testExpectation(output, expectation);

    Optional<Integer> support = output
      .getElementDataStore()
      .getGraphInteger(0, SubgraphMiningPropertyKeys.SUPPORT);

    assertTrue("support property", support.isPresent());
    assertEquals("support value", Integer.valueOf(2), support.get());

    Optional<Integer> frequency = output
      .getElementDataStore()
      .getGraphInteger(0, SubgraphMiningPropertyKeys.FREQUENCY);

    assertTrue("frequency property", frequency.isPresent());
    assertEquals("frequency value", Integer.valueOf(2), frequency.get());

    Optional<String[]> categories = output
      .getElementDataStore()
      .getGraphStrings(0, SubgraphMiningPropertyKeys.CATEGORIES);

    assertTrue("categories array", categories.isPresent());
    assertEquals("categories size", 1, categories.get().length);

  }
}
