package org.biiig.dmgm.impl.algorithms.fsm.fsm;

import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.algorithms.fsm.common.SubgraphMiningPatternTest;

public class FSMPatternTest extends SubgraphMiningPatternTest {


  @Override
  protected Operator getOperator() {
    return new FrequentSubgraphs(0.6f, 10);
  }

}
