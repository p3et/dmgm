package org.biiig.dmgm.impl.graph_loader.tlf;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.Graph;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class TLFReaderTest extends DMGMTestBase {

  @Test
  public void testRead() throws IOException {

    GraphCollection database = getPredictableDatabase();

    for (int graphId = 0; graphId < database.size(); graphId++) {
      Graph graph = database.getGraph(graphId);

      for (int edgeId = 0; edgeId < graph.getEdgeCount(); edgeId++) {
        int sourceId = graph.getSourceId(edgeId);
        assertTrue("inconsistent source id " + sourceId, sourceId < graph.getVertexCount());

        int targetId = graph.getTargetId(edgeId);
        assertTrue("inconsistent target id " + edgeId, targetId < graph.getVertexCount());
      }
    }

    assertEquals("graph count", 10, database.size());
  }

}