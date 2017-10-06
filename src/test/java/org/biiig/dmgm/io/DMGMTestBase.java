package org.biiig.dmgm.io;

import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.api.model.source.DMGraphDataSource;
import org.biiig.dmgm.api.model.to_string.DMGraphFormatter;
import org.biiig.dmgm.impl.model.collection.InMemoryGraphCollection;
import org.biiig.dmgm.impl.model.source.tlf.TLFDataSource;
import org.biiig.dmgm.impl.model.graph.SourceTargetMuxFactory;
import org.biiig.dmgm.impl.to_string.CAMFormatter;

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

  protected boolean equal(DMGraphCollection expected, DMGraphCollection actual) {
    if (expected.size() != actual.size()) {
      System.out.println("Expected " + expected + " graphs but found only " + actual.size() );
    } else {

      String[] expectedStrings = new String[expected.size()];
      String[] actualStrings = new String[expected.size()];

      DMGraphFormatter formatter = new CAMFormatter();

      for (DMGraph graph : expected) {
        System.out.println(formatter.format(graph));
      }

    }

    return false;
  }
}
