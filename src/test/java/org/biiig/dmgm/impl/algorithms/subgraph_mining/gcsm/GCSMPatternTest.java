package org.biiig.dmgm.impl.algorithms.subgraph_mining.gcsm;

import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPatternTest;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.GeneralizedCharacteristicSubgraphs;

public class GCSMPatternTest extends SubgraphMiningPatternTest {


  @Override
  protected Operator getOperator() {
    return new GeneralizedCharacteristicSubgraphs(0.6f, 10, (f, t) -> new int[] {0});
  }

}
