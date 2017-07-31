package org.biiig.dmgm.tfsm.dmg_gspan;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class DMGgSpanTest {

  @Test
  public void mine() throws Exception {
    mine(1.0f, 702);
    mine(0.8f, 3 * 702);
  }

  private void mine(float minSupportThreshold, int expectedResultSize) throws IOException {
    String inputPath = DMGgSpan.class.getResource("/samples/predictable.tlf").getFile();
    DMGgSpan gSpan = new DMGgSpan(inputPath, minSupportThreshold, 15);
    gSpan.mine();
    assertEquals(expectedResultSize, gSpan.getResult().size());
  }

}