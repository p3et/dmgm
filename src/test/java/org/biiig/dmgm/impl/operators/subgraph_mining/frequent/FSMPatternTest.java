package org.biiig.dmgm.impl.operators.subgraph_mining.frequent;

import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.impl.operators.subgraph_mining.FrequentSubgraphs;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningPatternTest;

public class FSMPatternTest extends SubgraphMiningPatternTest {


  @Override
  protected CollectionOperator getOperator() {
    return new FrequentSubgraphs(0.6f, 10);
  }

}
