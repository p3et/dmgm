package org.biiig.dmgm.tfsm.dmg_gspan;

import org.apache.commons.lang3.time.StopWatch;
import org.biiig.dmgm.tfsm.dmg_gspan.vector_mining.CrossLevelFrequentVectors;
import org.biiig.dmgm.tfsm.dmg_gspan.vector_mining.CrossLevelFrequentVectorsBottomUp;
import org.biiig.dmgm.tfsm.dmg_gspan.vector_mining.CrossLevelFrequentVectorsTopDown;
import org.junit.Test;

import java.io.IOException;

public class GenSpanBench {

  public static final float THRESHOLD = 1.0f;
  public static final int K_MAX = 1;
    //String inputPath = GenSpan.class.getResource("/samples/multi_level.tlf").getFile();
  String inputPath = "/home/peet/fb10.tlf";


  @Test
  public void mineGSpan() throws IOException {
    DMGgSpan gSpan = new DMGgSpan(inputPath, THRESHOLD, K_MAX);
    gSpan.mine();
  }

  @Test
  public void mineBaseLine() throws IOException {
    GenSpanBaseline gSpan = new GenSpanBaseline(inputPath, THRESHOLD, K_MAX);
    gSpan.mine();
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
  }


  @Test
  public void bench() throws Exception {
    for (int round : new int[] {0}) {
      for (int sf : new int[] {333}) {
        for (float threshold : new float[] {0.4f, 0.2f, 0.1f}) {
          String inputPath = "/home/peet/gfsm_bench/" + sf + "_" + round + ".tlf";

          for (int kmax : new int[] {3, 5, 7}) {
            StopWatch watch = new StopWatch();
            watch.start();
            DMGgSpan gSpan = new DMGgSpan(inputPath, threshold, kmax);
            gSpan.mine();
            watch.stop();
            System.out.println(
              "g|" + sf + "|" + round + "|" + threshold + "|" + kmax + "|" +
                gSpan.getResult().size() + "|" + watch.getTime());

            watch = new StopWatch();
            watch.start();
            GenSpanBaseline baseline = new GenSpanBaseline(inputPath, threshold, kmax);
            baseline.mine();
            watch.stop();
            System.out.println(
              "b|" + sf + "|" + round + "|" + threshold + "|" + kmax + "|" +
                baseline.getResult().size() + "|" + watch.getTime());

            watch = new StopWatch();
            watch.start();
            GenSpan genSpan = new GenSpan(inputPath, threshold, new
              CrossLevelFrequentVectorsTopDown(), kmax);
            genSpan.mine();
            watch.stop();
            System.out.println(
              "d|" + sf + "|" + round + "|" + threshold + "|" + kmax + "|" +
                genSpan.getResult().size() + "|" + watch.getTime());

            watch = new StopWatch();
            watch.start();
            genSpan = new GenSpan(
              inputPath, threshold, new CrossLevelFrequentVectorsBottomUp(), kmax);
            genSpan.mine();
            watch.stop();
            System.out.println(
              "u|" + sf + "|" + round + "|" + threshold + "|" + kmax + "|" +
                genSpan.getResult().size() + "|" + watch.getTime());
          }
        }
      }
    }
  }
}