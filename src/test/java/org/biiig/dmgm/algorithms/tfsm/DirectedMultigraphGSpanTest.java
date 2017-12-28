package org.biiig.dmgm.algorithms.tfsm;

import org.biiig.dmgm.api.algorithms.tfsm.Algorithm;
import org.biiig.dmgm.impl.algorithms.tfsm.FrequentSubgraphs;
import org.biiig.dmgm.impl.algorithms.tfsm.TFSMConfig;

public class DirectedMultigraphGSpanTest extends TransactionalFSMTest {


  @Override
  Algorithm getMiner(TFSMConfig config) {
    return new FrequentSubgraphs().withMinSupport(config.getMinSupport()).withMaxEdgeCount(config.getMaxEdgeCount());
  }
}