package org.biiig.dmgm.impl.operators.subgraph_mining.gcsm;

import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningPatternTest;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedCharacteristicSubgraphs;

public class GCSMPatternTest extends SubgraphMiningPatternTest {


  @Override
  protected CollectionOperator getOperator() {
    return new GeneralizedCharacteristicSubgraphs(0.6f, 10, (f, t) -> new int[] {0});
  }

}
