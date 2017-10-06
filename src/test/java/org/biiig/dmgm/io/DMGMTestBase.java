package org.biiig.dmgm.io;

import org.biiig.dmgm.api.DMGraphDatabase;
import org.biiig.dmgm.api.io.DMGraphDataSource;
import org.biiig.dmgm.impl.InMemoryDatabase;
import org.biiig.dmgm.impl.io.tlf.TLFDataSource;
import org.biiig.dmgm.impl.model.graph.SourceTargetMuxFactory;

import java.io.IOException;

/**
 * Created by peet on 11.08.17.
 */
public class DMGMTestBase {
  protected DMGraphDatabase getPredictableDatabase(float minSupportThreshold) throws IOException {
    String inputPath = TLFDataSource.class.getResource("/samples/predictable.tlf").getFile();
    DMGraphDatabase database = new InMemoryDatabase();
    DMGraphDataSource reader = new TLFDataSource(inputPath);
    reader.loadWithMinLabelSupport(database, new SourceTargetMuxFactory(), minSupportThreshold);
    return database;
  }
}
