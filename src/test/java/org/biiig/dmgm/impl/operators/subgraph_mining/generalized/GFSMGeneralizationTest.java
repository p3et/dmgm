package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedFrequentSubgraphs;

public class GFSMGeneralizationTest extends GeneralizationTestBase {

  @Override
  public CollectionOperator getOperator() {
    return new GeneralizedFrequentSubgraphs(1.0f, 10);
  }
}
