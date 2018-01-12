package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.operators.subgraph_mining.CharacteristicSubgraphs;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningPatternTest;

public class CSMPatternTest extends SubgraphMiningPatternTest {


  @Override
  protected Operator getOperator() {
    return new CharacteristicSubgraphs(0.6f, 10, (f, t) -> new int[] {0});
  }

}
