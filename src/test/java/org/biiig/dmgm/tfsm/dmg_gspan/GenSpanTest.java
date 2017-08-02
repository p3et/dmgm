package org.biiig.dmgm.tfsm.dmg_gspan;

import org.biiig.dmgm.tfsm.dmg_gspan.impl.mining.DircetedMulitgraphGSpan;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.mining.GenSpan;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.mining.GenSpanBaseline;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.vector_mining.CrossLevelFrequentVectors;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.vector_mining.CrossLevelFrequentVectorsBottomUp;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.vector_mining.CrossLevelFrequentVectorsTopDown;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GenSpanTest {

  public static final float THRESHOLD = 1.0f;
  public static final int K_MAX = 1;
    String inputPath = GenSpan.class.getResource("/samples/multi_level.tlf").getFile();


  @Test
  public void mineGSpan() throws IOException {
    DircetedMulitgraphGSpan gSpan = new DircetedMulitgraphGSpan(inputPath, THRESHOLD, K_MAX);
    gSpan.mine();
    assertEquals(7, gSpan.getResult().size());
  }

  @Test
  public void mineBaseLine() throws IOException {
    GenSpanBaseline gSpan = new GenSpanBaseline(inputPath, THRESHOLD, K_MAX);
    gSpan.mine();
    gSpan.printResult();
    assertEquals(35, gSpan.getResult().size());
  }

  @Test
  public void mineBottomUp() throws Exception {
    mine(THRESHOLD, 35, new CrossLevelFrequentVectorsBottomUp());
  }

  @Test
  public void mineTopDown() throws Exception {
    mine(THRESHOLD, 35, new CrossLevelFrequentVectorsTopDown());
  }

  private void mine(float minSupportThreshold, int expectedResultSize,
    CrossLevelFrequentVectors gfvm) throws IOException {
    GenSpan gSpan = new GenSpan(inputPath, minSupportThreshold, gfvm, K_MAX);
    gSpan.mine();
    gSpan.printResult();
    assertEquals(expectedResultSize, gSpan.getResult().size());
  }

}