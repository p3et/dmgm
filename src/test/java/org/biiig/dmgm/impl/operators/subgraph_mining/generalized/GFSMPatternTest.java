package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import org.biiig.dmgm.api.HyperVertexOperator;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedFrequentSubgraphs;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningPatternTest;

public class GFSMPatternTest extends SubgraphMiningPatternTest {


  @Override
  protected HyperVertexOperator getOperator() {
    return new GeneralizedFrequentSubgraphs(0.6f, 10);
  }

}
