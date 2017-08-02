package org.biiig.dmgm.model;

import org.biiig.dmgm.impl.model.SourceTargetMuxFactory;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Created by peet on 02.08.17.
 */
public class DirectedGraphTest {

  @Test
  public void testGetterAndSetter() throws Exception {
    DirectedGraphFactory factory = getFactory();

    DirectedGraph graph = factory.create(2, 2);

    int[] data0 = new int[] {0};
    int[] data1 = new int[] {1, 1};

    graph.setVertex(0, data0);
    graph.setVertex(1, data1);

    graph.setEdge(0, 0, 0, data0);
    graph.setEdge(1, 0, 1, data1);

    assertEquals("vertex count", 2, graph.getVertexCount());
    assertEquals("vertex data 0", data0, graph.getVertexData(0));
    assertEquals("vertex data 1", data1, graph.getVertexData(1));
    assertEquals("edge count", 2, graph.getEdgeCount());
    assertEquals("edge data 0", data0, graph.getEdgeData(0));
    assertEquals("edge data 1", data1, graph.getEdgeData(1));
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


  DirectedGraphFactory getFactory() {
    return new SourceTargetMuxFactory();
  }

}