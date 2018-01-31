package org.biiig.dmgm.impl.graph;

import org.biiig.dmgm.api.CachedGraph;
import org.junit.Test;

public class SourceTargetMuxTest extends SingleLabelDirectedSmallGraphTest {
  @Test
  public void testGetterAndSetter() throws Exception {
    int lab0 = 0;
    int lab1 = 1;

    CachedGraph graph = new CachedGraphBase(
      0L,
      0,
      new int[] {lab0, lab1},
      new int[] {lab0, lab1},
      new int[] {0, 0},
      new int[] {0, 1}
    );

    test(graph, lab0, lab1);
  }
}