package org.biiig.dmgm.algorithms.tfsm;

import org.biiig.dmgm.api.model.collection.GraphCollection;
import org.biiig.dmgm.api.algorithms.tfsm.Operator;
import org.biiig.dmgm.impl.algorithms.tfsm.TFSMConfig;
import org.biiig.dmgm.impl.model.collection.InMemoryGraphCollection;
import org.biiig.dmgm.io.DMGMTestBase;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public abstract class TransactionalFSMTest extends DMGMTestBase {

  abstract Operator getMiner(TFSMConfig config);

  @Test
  public void mine10() throws Exception {
    mine(1.0f, 702);
  }

  @Test
  public void mine08() throws Exception {
    mine(0.8f, 2106);
  }

  private void mine(float minSupportThreshold, int expectedResultSize) throws IOException {
    TFSMConfig config = new TFSMConfig(minSupportThreshold, 100);
    Operator fsm = getMiner(config);
    GraphCollection input = getPredictableDatabase(minSupportThreshold);
    GraphCollection output = new InMemoryGraphCollection();

    fsm.execute(input, output);
    assertEquals(expectedResultSize, output.size());
  }

}