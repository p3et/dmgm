package org.biiig.dmgm.io;

import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.io.DataSource;
import org.biiig.dmgm.impl.InMemoryDatabase;
import org.biiig.dmgm.impl.io.tlf.TLFDataSource;
import org.biiig.dmgm.impl.model.graph.SourceTargetMuxFactory;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


/**
 * Created by peet on 04.08.17.
 */
public class TLFReaderTest {

  @Test
  public void testRead() throws IOException {
    String inputPath = TLFDataSource.class.getResource("/samples/predictable.tlf").getFile();
    Database database = new InMemoryDatabase();

    DataSource reader = new TLFDataSource(inputPath);

    reader.load(database, new SourceTargetMuxFactory(), 0.8f);

    assertEquals("vertex dictionary size", 4, database.getVertexDictionary().size());
    assertEquals("edge dictionary size", 5, database.getEdgeDictionary().size());
    assertEquals("graph count", 10, database.getGraphCount());


  }
}