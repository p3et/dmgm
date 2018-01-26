package org.biiig.dmgm;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.api.DMGraphFormatter;
import org.biiig.dmgm.api.HyperVertexOperator;
import org.biiig.dmgm.impl.loader.GDLLoader;
import org.biiig.dmgm.impl.loader.TLFLoader;
import org.biiig.dmgm.impl.to_string.cam.CAMGraphFormatter;
import org.biiig.dmgm.impl.to_string.edge_list.ELGraphFormatter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;

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
      new CAMGraphFormatter(expected.getLabelDictionary(), expected.getLabelDictionary());

    DMGraphFormatter valueFormatter =
      new ELGraphFormatter(expected.getLabelDictionary(), expected.getLabelDictionary());

    Map<String, String> canonicalLabels = Maps.newHashMapWithExpectedSize(expected.size());
    for (SmallGraph graph : expected) {
      canonicalLabels.put(keyFormatter.format(graph), valueFormatter.format(graph));
    }

    return canonicalLabels;
  }

  protected void print(GraphCollection graphCollection) {
    DMGraphFormatter formatter =
      new ELGraphFormatter(graphCollection.getLabelDictionary(), graphCollection.getLabelDictionary());

    System.out.println(graphCollection.size());

    for (SmallGraph graph : graphCollection) {
      System.out.println(formatter.format(graph));
    }
  }

  protected void runAndTestExpectation(HyperVertexOperator operator, String inputGDL, String expectedGDL) {
    GraphCollection input = GDLLoader
      .fromString(inputGDL)
      .getGraphCollection();

    GraphCollection output = operator.apply(input);

    testExpectation(output, expectedGDL);
  }

  protected void testExpectation(GraphCollection output, String expectedGDL) {
    GraphCollection expected = GDLLoader
      .fromString(expectedGDL)
      .getGraphCollection();

    assertTrue("constistent", isConsistent(output));
    assertTrue("equals", equal(expected, output));
  }

  private boolean isConsistent(GraphCollection collection) {
    boolean consistent = true;

    for (SmallGraph graph : collection) {
      for (int vertexId = 0; vertexId < graph.getVertexCount(); vertexId++ ) {
        int vertexLabel = graph.getVertexLabel(vertexId);
        String translation = collection.getLabelDictionary().translate(vertexLabel);

        consistent = consistent && translation != null;

        if (translation == null) {
          System.out.println("no translation found for integer vertex label " + vertexLabel);
        }
      }

      for (int edgeId = 0; edgeId < graph.getEdgeCount(); edgeId++ ) {
        int edgeLabel = graph.getEdgeLabel(edgeId);
        String translation = collection.getLabelDictionary().translate(edgeLabel);

        consistent = consistent && translation != null;

        if (translation == null) {
          System.out.println("no translation found for integer edge label " + edgeLabel);
        }
      }
    }

    return consistent;
  }
}
