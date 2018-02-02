package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.operators.subgraph_mining.CharacteristicSubgraphs;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningThresholdTest;

public class CSMThresholdTest extends SubgraphMiningThresholdTest {

  @Override
  protected CollectionOperator getOperator(GraphDB db, float minSupportRel, int maxEdgeCount) {
    return new CharacteristicSubgraphs(db, minSupportRel, maxEdgeCount);
  }
}