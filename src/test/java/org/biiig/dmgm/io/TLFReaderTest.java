package org.biiig.dmgm.io;

import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.io.DataSource;
import org.biiig.dmgm.impl.InMemoryDatabase;
import org.biiig.dmgm.impl.io.tlf.TLFDataSource;
import org.biiig.dmgm.impl.model.graph.SourceTargetMuxFactory;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class TLFReaderTest extends DMGMTestBase {

  @Test
  public void testRead() throws IOException {
    float minSupportThreshold = 0.8f;

    Database database = getPredictableDatabase(minSupportThreshold);

    assertEquals("vertex dictionary size", 4, database.getVertexDictionary().size());
    assertEquals("edge dictionary size", 5, database.getEdgeDictionary().size());
    assertEquals("graph count", 10, database.getGraphCount());
  }

}