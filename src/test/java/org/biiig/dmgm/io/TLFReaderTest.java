package org.biiig.dmgm.io;

import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.api.model.graph.DMGraph;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class TLFReaderTest extends DMGMTestBase {

  @Test
  public void testRead() throws IOException {
    float minSupportThreshold = 0.8f;

    DMGraphCollection database = getPredictableDatabase(minSupportThreshold);

    for (int graphId = 0; graphId < database.getGraphCount(); graphId++) {
      DMGraph graph = database.getGraph(graphId);

      for (int edgeId = 0; edgeId < graph.getEdgeCount(); edgeId++) {
        int sourceId = graph.getSourceId(edgeId);
        assertTrue("inconsistent source id " + sourceId, sourceId < graph.getVertexCount());

        int targetId = graph.getTargetId(edgeId);
        assertTrue("inconsistent target id " + edgeId, targetId < graph.getVertexCount());
      }
    }

    assertEquals("vertex dictionary size", 4, database.getVertexDictionary().size());
    assertEquals("edge dictionary size", 5, database.getEdgeDictionary().size());
    assertEquals("graph count", 10, database.getGraphCount());
  }

}