package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import com.google.common.collect.Maps;
import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPatternTest;

public class GFSMPatternTest extends SubgraphMiningPatternTest {


  @Override
  protected Operator getOperator() {
    return new GeneralizedFrequentSubgraphs(0.6f, 10);
  }

}
