package org.biiig.dmgm.impl.operators.subgraph_mining.gcsm;

import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedCharacteristicSubgraphs;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningThresholdTest;

public class GCSMThresholdTest extends SubgraphMiningThresholdTest {

  @Override
  protected CollectionOperator getOperator(GraphDB db, float minSupportRel, int maxEdgeCount) {
    return new GeneralizedCharacteristicSubgraphs(db, minSupportRel, maxEdgeCount);
  }
}