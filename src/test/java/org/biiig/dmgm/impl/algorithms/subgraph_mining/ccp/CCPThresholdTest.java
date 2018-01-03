package org.biiig.dmgm.impl.algorithms.subgraph_mining.ccp;

import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningThresholdTest;

public class CCPThresholdTest extends SubgraphMiningThresholdTest {

  @Override
  protected Operator getOperator(float minSupportRel, int maxEdgeCount) {
    return new CategoryCharacteristicSubgraphs(minSupportRel, maxEdgeCount, g -> "", (f, t) -> true);
  }
}