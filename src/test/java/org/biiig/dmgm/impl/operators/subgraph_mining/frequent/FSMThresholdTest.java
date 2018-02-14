package org.biiig.dmgm.impl.operators.subgraph_mining.frequent;

import org.biiig.dmgm.TestConstants;
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.operators.CollectionToCollectionOperator;
import org.biiig.dmgm.impl.operators.fsm.FrequentSimpleSubgraphs;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningThresholdTestBase;

public class FSMThresholdTest extends SubgraphMiningThresholdTestBase {

  protected CollectionToCollectionOperator getOperator(PropertyGraphDB db, boolean b, float minSupportRel, int maxEdgeCount) {
    return new FrequentSimpleSubgraphs(db, TestConstants.PARALLEL, minSupportRel, maxEdgeCount);
  }
}