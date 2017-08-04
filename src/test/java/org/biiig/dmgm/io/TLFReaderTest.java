package org.biiig.dmgm.io;

import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.io.DataReader;
import org.biiig.dmgm.impl.InMemoryDatabase;
import org.biiig.dmgm.impl.io.TLFReader;
import org.biiig.dmgm.impl.model.SourceTargetMuxFactory;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by peet on 04.08.17.
 */
public class TLFReaderTest {

  @Test
  public void testRead() throws IOException {
    String inputPath = TLFReader.class.getResource("/samples/predictable.tlf").getFile();
    Database database = new InMemoryDatabase();

    DataReader reader = new TLFReader(inputPath, new SourceTargetMuxFactory(), database);

    reader.read();
  }
}