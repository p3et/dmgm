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
import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.db.Property;
import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.api.operators.CollectionToCollectionOperator;
import org.biiig.dmgm.impl.db.GdlLoader;
import org.biiig.dmgm.impl.db.InMemoryGraphDbSupplier;
import org.biiig.dmgm.impl.db.TlfLoader;
import org.biiig.dmgm.to_string.cam.CAMGraphFormatter;
import org.biiig.dmgm.to_string.edge_list.ELGraphFormatter;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.Assert.assertTrue;

/**
 * Superclass of tests to provide them with useful utils.
 */
public class DmgmTestBase {
  /**
   *
   */
  protected static final Supplier<PropertyGraphDb> DB_FACTORY = new InMemoryGraphDbSupplier(true);

  /**
   * Returns a sample database with a predictable amount of frequent patterns.
   * A description of this database can be found in Section 5.3 of
   * Petermann et al. "DIMSpan: Transactional Frequent Subgraph Mining with Distributed In-Memory Dataflow Systems"
   * @see <a href="https://dl.acm.org/citation.cfm?id=3148064">ACM Digital Library</a>
   *
   * @return database
   */
  protected PropertyGraphDb getPredictableDatabase() {
    String inputPath = TlfLoader.class.getResource("/samples/predictable.tlf").getFile();
    return new TlfLoader(DB_FACTORY, inputPath).get();
  }

  private boolean equal(PropertyGraphDb db, long expId, long resId, boolean includeProperties) {
    Collection<CachedGraph> expected = db.getCachedCollection(expId);
    Collection<CachedGraph> result = db.getCachedCollection(resId);

    boolean equalSize = expected.size() == result.size();

    if (!equalSize) {
      System.out.println("Expected " + expected.size() + " graphs but found " + result.size() );
    }

    boolean allFound = compare(db, "Not found :", expected, result, includeProperties);
    boolean allExpected = compare(db, "Unexpected :", result, expected, includeProperties);

    return equalSize && allFound && allExpected;
  }

  private boolean compare(PropertyGraphDb db, String msg, Collection<CachedGraph> aCol, Collection<CachedGraph> bCol, boolean includeProperties) {
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

  private Map<String, String> getLabelMap(Collection<CachedGraph> expected, PropertyGraphDb db, boolean includeProperties) {
    Function<CachedGraph, String> keyFormatter = new CAMGraphFormatter(db);

    Function<CachedGraph, String> valueFormatter = new ELGraphFormatter(db);

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


      String canonicalLabel = keyFormatter.apply(graph) + "\t" + propertyLabel;
      String printLabel = valueFormatter.apply(graph) + "\t" + propertyLabel;
      canonicalLabels.put(canonicalLabel, printLabel);
    }

    return canonicalLabels;
  }

  protected void runAndTestExpectation(
    Function<PropertyGraphDb, CollectionToCollectionOperator> operatorFactory, String gdl, boolean includeProperties) {

    PropertyGraphDb db = new GdlLoader(DB_FACTORY, gdl).get();

    int inLabel = db.encode(TestConstants.INPUT_GRAPH_LABEL);
    long[] inIds = db.queryElements(l -> l == inLabel);
    long inId = db.createCollection(inLabel, inIds);

    int exLabel = db.encode(TestConstants.EXPECTATION_GRAPH_LABEL);
    long[] exIds = db.queryElements(l -> l == exLabel);
    long exId = db.createCollection(exLabel, exIds);
    long outId = operatorFactory.apply(db).apply(inId);

    assertTrue(equal(db, exId, outId, includeProperties));
  }
}
