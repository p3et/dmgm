package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningThresholdTest;

public class GFSMThresholdTest extends SubgraphMiningThresholdTest {

  @Override
  protected Operator getOperator(float minSupportRel, int maxEdgeCount) {
    return new GeneralizedFrequentSubgraphs(minSupportRel, maxEdgeCount);
  }
}