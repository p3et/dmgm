package org.biiig.dmgm.impl.algorithms.fsm.fsm;

import org.biiig.dmgm.api.Operator;

public class DirectedMultigraphGSpanTest extends TransactionalFSMTest {

  @Override
  Operator getMiner(float minSupportRel, int maxEdgeCount) {
    return new FrequentSubgraphs(minSupportRel, maxEdgeCount);
  }
}