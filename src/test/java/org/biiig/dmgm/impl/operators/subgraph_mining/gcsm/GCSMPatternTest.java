package org.biiig.dmgm.impl.operators.subgraph_mining.gcsm;

import org.biiig.dmgm.api.HyperVertexOperator;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningPatternTest;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedCharacteristicSubgraphs;

public class GCSMPatternTest extends SubgraphMiningPatternTest {


  @Override
  protected HyperVertexOperator getOperator() {
    return new GeneralizedCharacteristicSubgraphs(0.6f, 10, (f, t) -> new int[] {0});
  }

}
