package org.biiig.dmgm;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.DMGraphFormatter;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.api.Property;
import org.biiig.dmgm.impl.loader.GDLLoader;
import org.biiig.dmgm.impl.loader.TLFLoader;
import org.biiig.dmgm.impl.to_string.cam.CAMGraphFormatter;
import org.biiig.dmgm.impl.to_string.edge_list.ELGraphFormatter;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static org.junit.Assert.assertTrue;

/**
 * Created by peet on 11.08.17.
 */
public class DMGMTestBase {
  String INPUT_GRAPH_LABEL = "IN";
  String EXPECTATION_GRAPH_LABEL = "EX";


  protected GraphDB getPredictableDatabase() throws IOException {
    String inputPath = TLFLoader.class.getResource("/samples/predictable.tlf").getFile();
    return TLFLoader
      .fromFile(inputPath)
      .get();
  }

  protected boolean equal(GraphDB db, long expId, long resId, boolean includeProperties) {
    Collection<CachedGraph> expected = db.getCachedCollection(expId);
    Collection<CachedGraph> result = db.getCachedCollection(resId);

    boolean equalSize = expected.size() == result.size();

    if (!equalSize) {
      System.out.println("Expected " + expected.size() + " graphs but found " + result.size() );
    }

    boolean allFound = compare(db, "Not found :", expId, expected, resId, result, includeProperties);
    boolean allExpected = compare(db, "Unexpected :", resId, result, expId, expected, includeProperties);

    return equalSize && allFound && allExpected;
  }

  private boolean compare(GraphDB db, String msg, long aId, Collection<CachedGraph> aCol, long bId, Collection<CachedGraph> bCol, boolean includeProperties) {
    Map<String, String> aMap = getLabelMap(aCol, db, includeProperties);
    Map<String, String> bMap = getLabelMap(bCol, db, includeProperties);

    Set<String> notInB = aMap.keySet();
    notInB.removeAll(bMap.keySet());

    if (!notInB.isEmpty()) {
      System.out.println(msg);
      notInB
        .forEach(x -> System.out.println(aMap.get(x)));
    }

    return notInB.isEmpty();
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

  private Map<String, String> getLabelMap(Collection<CachedGraph> expected, GraphDB db, boolean includeProperties) {
    DMGraphFormatter keyFormatter =
      new CAMGraphFormatter(db);

    DMGraphFormatter valueFormatter =
      new ELGraphFormatter(db);

    Map<String, String> canonicalLabels = Maps.newHashMapWithExpectedSize(expected.size());
    for (CachedGraph graph : expected) {

      String propertyLabel;
      if (includeProperties) {
        Property[] properties = db.getProperties(graph.getId());
        String[] propertyStrings = new String[properties.length];

        for (int i = 0; i < properties.length; i++)
          propertyStrings[i] = db.decode(properties[i].getKey()) + "=" + properties[i].getValue();

        propertyLabel = StringUtils.join(propertyStrings, ",");
      } else {
        propertyLabel = "";
      }


      String canonicalLabel = keyFormatter.format(graph) + "\t" + propertyLabel;
      String printLabel = valueFormatter.format(graph) + "\t" + propertyLabel;
      canonicalLabels.put(canonicalLabel, printLabel);
    }

    return canonicalLabels;
  }

//  protected void print(GraphCollection graphCollection) {
//    DMGraphFormatter formatter =
//      new ELGraphFormatter(graphCollection.getLabelDictionary());
//
//    System.out.println(graphCollection.size());
//
//    for (SmallGraph graph : graphCollection) {
//      System.out.println(formatter.format(graph));
//    }
//  }

  protected void runAndTestExpectation(Function<GraphDB, CollectionOperator> operatorFactory, String gdl, boolean includeProperties) {
    GraphDB db = GDLLoader
      .fromString(gdl)
      .get();

    int inLabel = db.encode(INPUT_GRAPH_LABEL);
    long[] inIds = db.getElementsByLabel(l -> l == inLabel);
    long inId = db.createCollection(inLabel, inIds);

    int exLabel = db.encode(EXPECTATION_GRAPH_LABEL);
    long[] exIds = db.getElementsByLabel(l -> l == exLabel);
    long exId = db.createCollection(exLabel, exIds);
    long outId = operatorFactory.apply(db).apply(inId);

    assertTrue(equal(db, exId, outId, includeProperties));
  }

//  protected void testExpectation(GraphCollection output, String expectedGDL) {
//    GraphCollection expected = GDLLoader
//      .fromString(expectedGDL)
//      .getGraphCollection();
//
//    assertTrue("constistent", isConsistent(output));
//    assertTrue("equals", equal(expected, output, db));
//  }
//
//  private boolean isConsistent(GraphCollection collection) {
//    boolean consistent = true;
//
//    for (SmallGraph graph : collection) {
//      for (int vertexId = 0; vertexId < graph.getVertexCount(); vertexId++ ) {
//        int vertexLabel = graph.getVertexLabel(vertexId);
//        String translation = collection.getLabelDictionary().translate(vertexLabel);
//
//        consistent = consistent && translation != null;
//
//        if (translation == null) {
//          System.out.println("no translation found for integer vertex label " + vertexLabel);
//        }
//      }
//
//      for (int edgeId = 0; edgeId < graph.getEdgeCount(); edgeId++ ) {
//        int edgeLabel = graph.getEdgeLabel(edgeId);
//        String translation = collection.getLabelDictionary().translate(edgeLabel);
//
//        consistent = consistent && translation != null;
//
//        if (translation == null) {
//          System.out.println("no translation found for integer edge label " + edgeLabel);
//        }
//      }
//    }
//
//    return consistent;
//  }
}
