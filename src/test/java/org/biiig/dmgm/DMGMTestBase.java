package org.biiig.dmgm;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.DMGraphFormatter;
import org.biiig.dmgm.impl.graph_loader.tlf.TLFLoader;
import org.biiig.dmgm.impl.to_string.cam.CAMGraphFormatter;
import org.biiig.dmgm.impl.to_string.edge_list.ELGraphFormatter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by peet on 11.08.17.
 */
public class DMGMTestBase {
  protected GraphCollection getPredictableDatabase() throws IOException {
    String inputPath = TLFLoader.class.getResource("/samples/predictable.tlf").getFile();
    return TLFLoader
      .fromFile(inputPath)
      .getGraphCollection();
  }

  protected boolean equal(GraphCollection expected, GraphCollection actual) {
    boolean equal = expected.size() == actual.size();

    if (!equal) {
      System.out.println("Expected " + expected.size() + " graphs but found " + actual.size() );
    }

    Map<String, String> expectedLabels = getCanonicalPrintLabelMap(expected);
    Map<String, String> actualLabels = getCanonicalPrintLabelMap(actual);

    Set<String> notExpected = keyMinus(expectedLabels, actualLabels);
    Set<String> notFound = keyMinus(actualLabels, expectedLabels);

    if (notExpected.size() > 0) {
      System.out.println("Not expected :");
      print(notExpected);
    }

    if (notFound.size() > 0) {
      System.out.println("Not found :");
      print(notFound);
    }

    equal = equal && notExpected.isEmpty() && notFound.isEmpty();

    return equal;
  }

  private Set<String> keyMinus(Map<String, String> first, Map<String, String> second) {
    Set<String> difference = Sets.newHashSet();

    for (Map.Entry<String, String> secondEntry : second.entrySet()) {
      if (!first.containsKey(secondEntry.getKey())) {
        difference.add(secondEntry.getValue());
      }
    }

    return difference;
  }

  private void print(Set<String> strings) {
    for (String s : strings) {
      System.out.println(s);
    }
  }

  private Map<String, String> getCanonicalPrintLabelMap(GraphCollection expected) {
    DMGraphFormatter keyFormatter =
      new CAMGraphFormatter(expected.getVertexDictionary(), expected.getEdgeDictionary());

    DMGraphFormatter valueFormatter =
      new ELGraphFormatter(expected.getVertexDictionary(), expected.getEdgeDictionary());

    Map<String, String> canonicalLabels = Maps.newHashMapWithExpectedSize(expected.size());
    for (Graph graph : expected) {
      canonicalLabels.put(keyFormatter.format(graph), valueFormatter.format(graph));
    }

    return canonicalLabels;
  }

  protected void print(GraphCollection graphCollection) {
    DMGraphFormatter formatter =
      new ELGraphFormatter(graphCollection.getVertexDictionary(), graphCollection.getEdgeDictionary());

    System.out.println(graphCollection.size());

    for (Graph graph : graphCollection) {
      System.out.println(formatter.format(graph));
    }
  }
}
