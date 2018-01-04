package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPatternTest;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.csm.CharacteristicSubgraphs;

public class CSMPatternTest extends SubgraphMiningPatternTest {


  @Override
  protected Operator getOperator() {
    return new GeneralizedFrequentSubgraphs(0.6f, 10);
  }

}
