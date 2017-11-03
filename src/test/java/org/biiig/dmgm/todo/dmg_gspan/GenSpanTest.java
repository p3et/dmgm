//package org.biiig.dmgm.todo.dmg_gspan;
//
//import org.biiig.dmgm.impl.model.source.InMemoryDatabase;
//import org.biiig.dmgm.impl.algorithms.tfsm.DirectedMultigraphGSpan;
//import org.biiig.dmgm.todo.mining.GenSpan;
//import org.biiig.dmgm.todo.mining.GenSpanBaseline;
//import org.biiig.dmgm.todo.vector_mining.CrossLevelFrequentVectors;
//import org.biiig.dmgm.todo.vector_mining.CrossLevelFrequentVectorsBottomUp;
//import org.biiig.dmgm.todo.vector_mining.CrossLevelFrequentVectorsTopDown;
//import org.junit.Test;
//
//import java.source.IOException;
//
//import static org.junit.Assert.assertEquals;
//
//public class GenSpanTest {
//
//  public static final float THRESHOLD = 1.0f;
//  public static final int K_MAX = 1;
//    String inputPath = GenSpan.class.getResource("/samples/multi_level.tlf").getFile();
//
//
//  @Test
//  public void mineGSpan() throws IOException {
//    DirectedMultigraphGSpan gSpan = new DirectedMultigraphGSpan(inputPath, THRESHOLD, K_MAX);
//    gSpan.execute(new InMemoryDatabase());
//    assertEquals(7, gSpan.getResult().size());
//  }
//
//  @Test
//  public void mineBaseLine() throws IOException {
//    GenSpanBaseline gSpan = new GenSpanBaseline(inputPath, THRESHOLD, K_MAX);
//    gSpan.execute();
//    assertEquals(35, gSpan.getResult().size());
//  }
//
//  @Test
//  public void mineBottomUp() throws Exception {
//    execute(THRESHOLD, 35, new CrossLevelFrequentVectorsBottomUp());
//  }
//
//  @Test
//  public void mineTopDown() throws Exception {
//    execute(THRESHOLD, 35, new CrossLevelFrequentVectorsTopDown());
//  }
//
//  private void execute(float minSupportThreshold, int expectedResultSize,
//    CrossLevelFrequentVectors gfvm) throws IOException {
//    GenSpan gSpan = new GenSpan(inputPath, minSupportThreshold, gfvm, K_MAX);
//    gSpan.execute();
//    gSpan.printResult();
//    assertEquals(expectedResultSize, gSpan.getResult().size());
//  }
//
//}