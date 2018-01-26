package org.biiig.dmgm.impl.graph;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.SmallGraph;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by peet on 02.08.17.
 */
public abstract class SingleLabelDirectedSmallGraphTest extends DMGMTestBase {
  @Test
  public void testGetterAndSetter() throws Exception {
    GraphFactory factory = getFactory();

    SmallGraph graph = factory.create();

    int lab0 = 0;
    int lab1 = 1;

    graph.addVertex(lab0);
    graph.addVertex(lab1);

    graph.addEdge(0, 0, lab0);
    graph.addEdge(0, 1, lab1);

    assertEquals("vertex count", 2, graph.getVertexCount());
    assertEquals("vertex format 0", lab0, graph.getVertexLabel(0));
    assertEquals("vertex format 1", lab1, graph.getVertexLabel(1));
    assertEquals("edge count", 2, graph.getEdgeCount());
    assertEquals("edge format 0", lab0, graph.getEdgeLabel(0));
    assertEquals("edge format 1", lab1, graph.getEdgeLabel(1));
    assertEquals("edge source 0", 0, graph.getSourceId(0));
    assertEquals("edge target 0", 0, graph.getTargetId(0));
    assertEquals("edge source 1", 0, graph.getSourceId(1));
    assertEquals("edge target 1", 1, graph.getTargetId(1));

    int[] out = graph.getOutgoingEdgeIds(0);
    int[] expA = new int[] {0, 1};
    int[] expB = new int[] {1, 0};

    assertTrue("ougoing edges 0",
      Objects.deepEquals(out, expA) || Objects.deepEquals(out, expB));

    assertArrayEquals("incoming edges 0", new int[] {0}, graph.getIncomingEdgeIds(0));
    assertArrayEquals("ougoing edges 1", new int[0], graph.getOutgoingEdgeIds(1));
    assertArrayEquals("incoming edges 1", new int[] {1}, graph.getIncomingEdgeIds(1));
  }

  abstract GraphFactory getFactory();
}
