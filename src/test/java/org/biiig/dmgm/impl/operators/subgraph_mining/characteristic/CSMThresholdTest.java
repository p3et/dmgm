package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.operators.subgraph_mining.CharacteristicSubgraphs;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningThresholdTest;

public class CSMThresholdTest extends SubgraphMiningThresholdTest {

  @Override
  protected Operator getOperator(float minSupportRel, int maxEdgeCount) {
    return new CharacteristicSubgraphs(minSupportRel, maxEdgeCount, (f, t) -> new int[] {0});
  }
}