package org.biiig.dmgm.impl.algorithms.fsm.fsm;

import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.algorithms.fsm.common.SubgraphMiningThresholdTest;

public class FSMThresholdTest extends SubgraphMiningThresholdTest {

  @Override
  protected Operator getOperator(float minSupportRel, int maxEdgeCount) {
    return new FrequentSubgraphs(minSupportRel, maxEdgeCount);
  }
}