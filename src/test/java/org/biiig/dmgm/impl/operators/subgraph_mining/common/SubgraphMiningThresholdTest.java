package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.GraphDB;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public abstract class SubgraphMiningThresholdTest extends DMGMTestBase {

  protected abstract CollectionOperator getOperator(GraphDB db, float minSupportRel, int maxEdgeCount);

  @Test
  public void mine10() throws Exception {
    mine(1.0f, 702);
  }

  @Test
  public void mine08() throws Exception {
    mine(0.8f, 2106);
  }

  private void mine(float minSupportThreshold, int expectedResultSize) throws IOException {
    GraphDB db = getPredictableDatabase();

    CollectionOperator fsm = getOperator(db, minSupportThreshold, 20);

    long inId = db.createCollection(0, db.getAllGraphIds());

    fsm.sequential();

    Long outId = fsm.apply(inId);
    long[] graphIds = db.getGraphElementIds(outId).getLeft();
    assertEquals("sequential @ " + minSupportThreshold,expectedResultSize, graphIds.length);

    fsm.parallel();

    outId = fsm.apply(inId);
    graphIds = db.getGraphElementIds(outId).getLeft();
    assertEquals("parallel @ " + minSupportThreshold, expectedResultSize, graphIds.length);
  }

}