package org.biiig.dmgm.io;

import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.api.model.source.DMGraphDataSource;
import org.biiig.dmgm.impl.model.collection.InMemoryGraphCollection;
import org.biiig.dmgm.impl.model.source.tlf.TLFDataSource;
import org.biiig.dmgm.impl.model.graph.SourceTargetMuxFactory;

import java.io.IOException;

/**
 * Created by peet on 11.08.17.
 */
public class DMGMTestBase {
  protected DMGraphCollection getPredictableDatabase(float minSupportThreshold) throws IOException {
    String inputPath = TLFDataSource.class.getResource("/samples/predictable.tlf").getFile();
    DMGraphCollection database = new InMemoryGraphCollection();
    DMGraphDataSource reader = new TLFDataSource(inputPath);
    reader.loadWithMinLabelSupport(database, new SourceTargetMuxFactory(), minSupportThreshold);
    return database;
  }
}
