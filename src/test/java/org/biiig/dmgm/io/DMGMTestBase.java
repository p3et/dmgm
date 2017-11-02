package org.biiig.dmgm.io;

import com.google.common.collect.Sets;
import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.api.model.source.DMGraphDataSource;
import org.biiig.dmgm.api.model.to_string.DMGraphFormatter;
import org.biiig.dmgm.impl.model.collection.InMemoryGraphCollection;
import org.biiig.dmgm.impl.model.source.tlf.TLFDataSource;
import org.biiig.dmgm.impl.model.graph.SourceTargetMuxFactory;
import org.biiig.dmgm.impl.to_string.CAMGraphFormatter;

import java.io.IOException;
import java.util.Set;

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
    boolean equal = expected.size() == actual.size();

    if (!equal) {
      System.out.println("Expected " + expected.size() + " graphs but found only " + actual.size() );
    }

    Set<String> expectedLabels = toString(expected);
    Set<String> actualLabels = toString(actual);

    Set<String> notExpected = Sets.difference(actualLabels, expectedLabels);
    Set<String> notFound = Sets.difference(expectedLabels, actualLabels);

    if (notExpected.size() > 0) {
      System.out.println("Not expected :");
      print(notExpected);
    }

    if (notFound.size() > 0) {
      System.out.println("Not found :");
      print(notFound);
    }

    equal = notExpected.isEmpty() && notFound.isEmpty();

    return equal;
  }

  private void print(Set<String> strings) {
    for (String s : strings) {
      System.out.println(s);
    }
  }

  private Set<String> toString(DMGraphCollection expected) {
    DMGraphFormatter formatter =
      new CAMGraphFormatter(expected.getVertexDictionary(), expected.getEdgeDictionary());

    Set<String> canonicalLabels = Sets.newHashSetWithExpectedSize(expected.size());
    for (DMGraph graph : expected) {
      canonicalLabels.add(formatter.format(graph));
    }
    return canonicalLabels;
  }

  protected void print(DMGraphCollection graphCollection) {
    DMGraphFormatter formatter =
      new CAMGraphFormatter(graphCollection.getVertexDictionary(), graphCollection.getEdgeDictionary());

    System.out.println(graphCollection.size());

    for (DMGraph graph : graphCollection) {
      System.out.println(formatter.format(graph));
    }
  }
}
