package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import org.biiig.dmgm.api.HyperVertexOperator;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedFrequentSubgraphs;

public class GFSMGeneralizationTest extends GeneralizationTestBase {

  @Override
  public HyperVertexOperator getOperator() {
    return new GeneralizedFrequentSubgraphs(1.0f, 10);
  }
}
