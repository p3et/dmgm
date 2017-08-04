package org.biiig.dmgm.io;

import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.io.DataSource;
import org.biiig.dmgm.impl.InMemoryDatabase;
import org.biiig.dmgm.impl.io.tlf.TLFFileReader;
import org.biiig.dmgm.impl.model.graph.SourceTargetMuxFactory;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by peet on 04.08.17.
 */
public class TLFReaderTest {

  @Test
  public void testRead() throws IOException {
    String inputPath = TLFFileReader.class.getResource("/samples/predictable.tlf").getFile();
    Database database = new InMemoryDatabase();

    DataSource reader = new TLFFileReader(inputPath);

    reader.load(database, new SourceTargetMuxFactory());
  }
}