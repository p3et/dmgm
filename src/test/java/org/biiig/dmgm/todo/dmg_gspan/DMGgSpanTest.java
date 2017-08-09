package org.biiig.dmgm.todo.dmg_gspan;

import org.biiig.dmgm.impl.algorithms.tfsm.DirectedMultigraphGSpan;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public class DMGgSpanTest {

  @Test
  public void mine() throws Exception {
    mine(1.0f, 702);
    mine(0.8f, 3 * 702);
  }

  private void mine(float minSupportThreshold, int expectedResultSize) throws IOException {
    String inputPath = DirectedMultigraphGSpan.class.getResource("/samples/predictable.tlf").getFile();
    DirectedMultigraphGSpan gSpan = new DirectedMultigraphGSpan(inputPath, minSupportThreshold, 15);
    gSpan.mine();
    assertEquals(expectedResultSize, gSpan.getResult().size());
  }

}