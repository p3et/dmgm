package org.biiig.dmgm.impl.algorithms.subgraph_mining.frequent;

import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.FrequentSubgraphs;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPatternTest;

public class FSMPatternTest extends SubgraphMiningPatternTest {


  @Override
  protected Operator getOperator() {
    return new FrequentSubgraphs(0.6f, 10);
  }

}
