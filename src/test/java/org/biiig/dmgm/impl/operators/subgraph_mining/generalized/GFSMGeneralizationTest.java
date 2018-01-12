package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedCharacteristicSubgraphs;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedFrequentSubgraphs;

public class GFSMGeneralizationTest extends GeneralizationTestBase {

  @Override
  public Operator getOperator() {
    return new GeneralizedFrequentSubgraphs(1.0f, 10);
  }
}
