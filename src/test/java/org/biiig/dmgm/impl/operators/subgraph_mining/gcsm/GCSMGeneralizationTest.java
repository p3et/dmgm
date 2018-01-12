package org.biiig.dmgm.impl.operators.subgraph_mining.gcsm;

import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedCharacteristicSubgraphs;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.GeneralizationTestBase;

import static org.junit.Assert.assertEquals;

public class GCSMGeneralizationTest extends GeneralizationTestBase {

  @Override
  public Operator getOperator() {
    return new GeneralizedCharacteristicSubgraphs(1.0f, 10, (f, t) -> new int[] {0});
  }
}
