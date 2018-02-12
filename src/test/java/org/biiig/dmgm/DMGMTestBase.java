/*
 * This file is part of Directed Multigraph Miner (DMGM).
 *
 * DMGM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DMGM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DMGM. If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * This file is part of Directed Multigraph Miner (DMGM).
 *
 * DMGM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DMGM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DMGM. If not, see <http://www.gnu.org/licenses/>.
 */

package org.biiig.dmgm;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.db.QueryElements;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.api.operators.CollectionToCollectionOperator;
import org.biiig.dmgm.impl.db.KeyObjectPair;
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


  protected PropertyGraphDB getPredictableDatabase() throws IOException {
    String inputPath = TLFLoader.class.getResource("/samples/predictable.tlf").getFile();
    return TLFLoader
      .fromFile(inputPath)
      .get();
  }

  protected boolean equal(PropertyGraphDB db, long expId, long resId, boolean includeProperties) {
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

  private boolean compare(PropertyGraphDB db, String msg, long aId, Collection<CachedGraph> aCol, long bId, Collection<CachedGraph> bCol, boolean includeProperties) {
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

  private Map<String, String> getLabelMap(Collection<CachedGraph> expected, PropertyGraphDB db, boolean includeProperties) {
    CachedGraphFormatter keyFormatter =
      new CAMGraphFormatter(db);

    CachedGraphFormatter valueFormatter =
      new ELGraphFormatter(db);

    Map<String, String> canonicalLabels = Maps.newHashMapWithExpectedSize(expected.size());
    for (CachedGraph graph : expected) {

      String propertyLabel;
      if (includeProperties) {
        KeyObjectPair[] properties = db.getProperties(graph.getId());
        String[] propertyStrings = new String[properties.length];

        for (int i = 0; i < properties.length; i++)
          propertyStrings[i] = db.decode(properties[i].getKey()) + "=" + properties[i].getValue();

        propertyLabel = StringUtils.join(propertyStrings, ",");
      } else {
        propertyLabel = "";
      }


      String canonicalLabel = keyFormatter.apply(graph) + "\t" + propertyLabel;
      String printLabel = valueFormatter.apply(graph) + "\t" + propertyLabel;
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
//    for (SmallGraph model : graphCollection) {
//      System.out.println(formatter.format(model));
//    }
//  }

  protected void runAndTestExpectation(Function<QueryElements, CollectionToCollectionOperator> operatorFactory, String gdl, boolean includeProperties) {
    PropertyGraphDB db = GDLLoader
      .fromString(gdl)
      .get();

    int inLabel = db.encode(INPUT_GRAPH_LABEL);
    long[] inIds = db.queryElements(l -> l == inLabel);
    long inId = db.createCollection(inLabel, inIds);

    int exLabel = db.encode(EXPECTATION_GRAPH_LABEL);
    long[] exIds = db.queryElements(l -> l == exLabel);
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
//    for (SmallGraph model : collection) {
//      for (int vertexId = 0; vertexId < model.getVertexCount(); vertexId++ ) {
//        int vertexLabel = model.getVertexLabel(vertexId);
//        String translation = collection.getLabelDictionary().translate(vertexLabel);
//
//        consistent = consistent && translation != null;
//
//        if (translation == null) {
//          System.out.println("no translation found for integer vertex label " + vertexLabel);
//        }
//      }
//
//      for (int edgeId = 0; edgeId < model.getEdgeCount(); edgeId++ ) {
//        int edgeLabel = model.getEdgeLabel(edgeId);
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
