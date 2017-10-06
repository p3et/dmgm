package org.biiig.dmgm.model;

import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.api.model.graph.DMGraphFactory;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by peet on 02.08.17.
 */
public abstract class SingleLabelDirectedGraphTest {
  @Test
  public void testGetterAndSetter() throws Exception {
    DMGraphFactory factory = getFactory();

    DMGraph graph = factory.create(2, 2);

    int lab0 = 0;
    int lab1 = 1;

    graph.setVertex(0, lab0);
    graph.setVertex(1, lab1);

    graph.setEdge(0, 0, 0, lab0);
    graph.setEdge(1, 0, 1, lab1);

    assertEquals("vertex count", 2, graph.getVertexCount());
    assertEquals("vertex label 0", lab0, graph.getVertexLabel(0));
    assertEquals("vertex label 1", lab1, graph.getVertexLabel(1));
    assertEquals("edge count", 2, graph.getEdgeCount());
    assertEquals("edge label 0", lab0, graph.getEdgeLabel(0));
    assertEquals("edge label 1", lab1, graph.getEdgeLabel(1));
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

  abstract DMGraphFactory getFactory();
}
