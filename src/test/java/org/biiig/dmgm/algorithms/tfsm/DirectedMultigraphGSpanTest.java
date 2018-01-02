package org.biiig.dmgm.algorithms.tfsm;

import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.algorithms.tfsm.FrequentSubgraphs;

public class DirectedMultigraphGSpanTest extends TransactionalFSMTest {

  @Override
  Operator getMiner(float minSupportRel, int maxEdgeCount) {
    return new FrequentSubgraphs(minSupportRel, maxEdgeCount);
  }
}