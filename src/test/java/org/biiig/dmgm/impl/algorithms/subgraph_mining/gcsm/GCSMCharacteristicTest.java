package org.biiig.dmgm.impl.algorithms.subgraph_mining.gcsm;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPropertyKeys;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.csm.CharacteristicSubgraphs;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.gcsm.GeneralizedCharacteristicSubgraphs;
import org.biiig.dmgm.impl.graph_loader.gdl.GDLLoader;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class GCSMCharacteristicTest extends DMGMTestBase {

  @Test
  public void testAlgorithm() {

    Operator operator = new GeneralizedCharacteristicSubgraphs(
      1.0f,
      10,
      (c, t) -> c.size() == 1 ? ArrayUtils.toPrimitive(c.keySet().toArray(new Integer[1])) : null
    );

    String inputGDL =
      ":X[(:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":X[(:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":Y[(:A)-[:a]->(:B)-[:b]->(:C)-[:b]->(:D)]" +
      ":Y[(:A)-[:a]->(:B)-[:b]->(:C)-[:b]->(:E)]";

    String expectation =
      "[(:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(:D)]" +
      "[(:A)-[:a]->(:B)-[:a]->(:C)]" +
      "[(:B)-[:a]->(:C)-[:a]->(:D)]" +
      "[(:B)-[:a]->(:C)]" +
      "[(:C)-[:a]->(:D)]" +
      "[(:A)-[:a]->(:B)-[:b]->(:C)]" +
      "[(:B)-[:b]->(:C)]" ;

    GraphCollection input = GDLLoader
      .fromString(inputGDL)
      .getGraphCollection();

    GraphCollection output = input.apply(operator);

    testExpectation(output, expectation);

    for (Graph graph : output) {
      Optional<BigDecimal> support = output
        .getElementDataStore()
        .getGraphBigDecimal(graph.getId(), SubgraphMiningPropertyKeys.SUPPORT);

      assertTrue("support property", support.isPresent());
      assertEquals("support value", BigDecimal.valueOf(1.0), support.get());
      String label = output.getLabelDictionary().translate(graph.getLabel());
      assertTrue("pattern label", label.equals("X") || label.equals("Y"));
    }
  }
}
