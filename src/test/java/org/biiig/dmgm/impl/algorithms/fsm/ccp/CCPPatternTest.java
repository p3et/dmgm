package org.biiig.dmgm.impl.algorithms.fsm.ccp;

import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.algorithms.fsm.common.SubgraphMiningPatternTest;

public class CCPPatternTest extends SubgraphMiningPatternTest {


  @Override
  protected Operator getOperator() {
    return new CategoryCharacteristicSubgraphs(0.6f, 10, g -> "", (f, t) -> true);
  }

}
