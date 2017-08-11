package org.biiig.dmgm.io;

import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.io.DataSource;
import org.biiig.dmgm.api.model.graph.DirectedGraph;
import org.biiig.dmgm.impl.InMemoryDatabase;
import org.biiig.dmgm.impl.io.tlf.TLFDataSource;
import org.biiig.dmgm.impl.model.graph.SourceTargetMuxFactory;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class TLFReaderTest extends DMGMTestBase {

  @Test
  public void testRead() throws IOException {
    float minSupportThreshold = 0.8f;

    Database database = getPredictableDatabase(minSupportThreshold);

    for (int graphId = 0; graphId < database.getGraphCount(); graphId++) {
      DirectedGraph graph = database.getGraph(graphId);

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