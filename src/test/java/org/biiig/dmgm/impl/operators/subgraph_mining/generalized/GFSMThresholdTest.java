package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedFrequentSubgraphs;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningThresholdTest;

public class GFSMThresholdTest extends SubgraphMiningThresholdTest {

  @Override
  protected CollectionOperator getOperator(GraphDB db, float minSupportRel, int maxEdgeCount) {
    return new GeneralizedFrequentSubgraphs(db, minSupportRel, maxEdgeCount);
  }
}