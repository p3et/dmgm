package org.biiig.dmgm.impl.algorithms.subgraph_mining.gcsm;

import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningThresholdTest;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.GeneralizedCharacteristicSubgraphs;

public class GCSMThresholdTest extends SubgraphMiningThresholdTest {

  @Override
  protected Operator getOperator(float minSupportRel, int maxEdgeCount) {
    return new GeneralizedCharacteristicSubgraphs(minSupportRel, maxEdgeCount, (f, t) -> new int[] {0});
  }
}