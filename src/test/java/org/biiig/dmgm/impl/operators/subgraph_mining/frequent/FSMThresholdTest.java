package org.biiig.dmgm.impl.operators.subgraph_mining.frequent;

import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.operators.subgraph_mining.FrequentSubgraphs;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningThresholdTest;

public class FSMThresholdTest extends SubgraphMiningThresholdTest {

  @Override
  protected Operator getOperator(float minSupportRel, int maxEdgeCount) {
    return new FrequentSubgraphs(minSupportRel, maxEdgeCount);
  }
}