package org.biiig.dmgm.impl.algorithms.tfsm;

import org.biiig.dmgm.api.Operator;

public class DirectedMultigraphGSpanTest extends TransactionalFSMTest {

  @Override
  Operator getMiner(float minSupportRel, int maxEdgeCount) {
    return new FrequentSubgraphs(minSupportRel, maxEdgeCount);
  }
}