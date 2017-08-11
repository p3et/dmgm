package org.biiig.dmgm.io;

import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.io.DataSource;
import org.biiig.dmgm.impl.InMemoryDatabase;
import org.biiig.dmgm.impl.io.tlf.TLFDataSource;
import org.biiig.dmgm.impl.model.graph.SourceTargetMuxFactory;

import java.io.IOException;

/**
 * Created by peet on 11.08.17.
 */
public class DMGMTestBase {
  protected Database getPredictableDatabase(float minSupportThreshold) throws IOException {
    String inputPath = TLFDataSource.class.getResource("/samples/predictable.tlf").getFile();
    Database database = new InMemoryDatabase();
    DataSource reader = new TLFDataSource(inputPath);
    reader.load(database, new SourceTargetMuxFactory(), minSupportThreshold);
    return database;
  }
}
