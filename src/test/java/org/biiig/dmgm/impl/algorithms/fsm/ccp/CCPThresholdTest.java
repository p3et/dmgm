package org.biiig.dmgm.impl.algorithms.fsm.ccp;

import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.algorithms.fsm.common.SubgraphMiningThresholdTest;

public class CCPThresholdTest extends SubgraphMiningThresholdTest {

  @Override
  protected Operator getOperator(float minSupportRel, int maxEdgeCount) {
    return new CategoryCharacteristicSubgraphs(minSupportRel, maxEdgeCount, g -> "", (f, t) -> true);
  }
}