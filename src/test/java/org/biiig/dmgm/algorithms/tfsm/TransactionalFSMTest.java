package org.biiig.dmgm.algorithms.tfsm;

import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.api.model.collection.GraphCollection;
import org.biiig.dmgm.io.DMGMTestBase;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public abstract class TransactionalFSMTest extends DMGMTestBase {

  abstract Operator getMiner(float minSupportRel, int maxEdgeCount);

  @Test
  public void mine10() throws Exception {
    mine(1.0f, 702);
  }

  @Test
  public void mine08() throws Exception {
    mine(0.8f, 2106);
  }

  private void mine(float minSupportThreshold, int expectedResultSize) throws IOException {
    Operator fsm = getMiner(minSupportThreshold, 20);
    GraphCollection input = getPredictableDatabase(minSupportThreshold);
    GraphCollection output = input.apply(fsm);


    System.out.println(output.size());
    assertEquals(expectedResultSize, output.size());
  }

}