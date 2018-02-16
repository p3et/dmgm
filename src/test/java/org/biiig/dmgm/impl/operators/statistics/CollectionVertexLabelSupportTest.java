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

package org.biiig.dmgm.impl.operators.statistics;

import com.google.common.collect.Maps;
import org.biiig.dmgm.DmgmTestBase;
import org.biiig.dmgm.TestConstants;
import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.api.operators.StatisticsExtractor;
import org.biiig.dmgm.impl.db.GdlLoader;
import org.biiig.dmgm.impl.db.InMemoryGraphDbSupplier;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CollectionVertexLabelSupportTest extends DmgmTestBase {

  private static final Map<String, Long> PREDICTABLE_EXPECTED_SUPPORT = Maps.newHashMap();

  static {
    PREDICTABLE_EXPECTED_SUPPORT.put("S", 10L);
    PREDICTABLE_EXPECTED_SUPPORT.put("A", 10L);
    PREDICTABLE_EXPECTED_SUPPORT.put("B", 9L);
    PREDICTABLE_EXPECTED_SUPPORT.put("C", 8L);
    PREDICTABLE_EXPECTED_SUPPORT.put("D", 7L);
    PREDICTABLE_EXPECTED_SUPPORT.put("E", 6L);
    PREDICTABLE_EXPECTED_SUPPORT.put("F", 5L);
    PREDICTABLE_EXPECTED_SUPPORT.put("G", 4L);
    PREDICTABLE_EXPECTED_SUPPORT.put("H", 3L);
    PREDICTABLE_EXPECTED_SUPPORT.put("J", 2L);
    PREDICTABLE_EXPECTED_SUPPORT.put("K", 1L);
  }

  private static final String GENERALIZED_GDL = "[(:A_A)-[:a]->(:B_B)][(:A)-[:a]->(:B_B_B)]";

  private static final Map<String, Long> GENERALIZED_EXPECTED_SUPPORT = Maps.newHashMap();

  static {
    GENERALIZED_EXPECTED_SUPPORT.put("A", 2L);
    GENERALIZED_EXPECTED_SUPPORT.put("A_A", 1L);
    GENERALIZED_EXPECTED_SUPPORT.put("B", 2L);
    GENERALIZED_EXPECTED_SUPPORT.put("B_B", 2L);
    GENERALIZED_EXPECTED_SUPPORT.put("B_B_B", 1L);
  }

  private static final Map<String, Long> GENDISABLED_EXPECTED_SUPPORT = Maps.newHashMap();

  static {
    GENDISABLED_EXPECTED_SUPPORT.put("A", 1L);
    GENDISABLED_EXPECTED_SUPPORT.put("A_A", 1L);
    GENDISABLED_EXPECTED_SUPPORT.put("B_B", 1L);
    GENDISABLED_EXPECTED_SUPPORT.put("B_B_B", 1L);
  }

  @Test
  public void testSimple() {
    PropertyGraphDb database = getPredictableDatabase();
    Long collectionId = database.createCollection(0, database.getGraphIds());

    test(database, collectionId, false, PREDICTABLE_EXPECTED_SUPPORT);
  }

  @Test
  public void testGeneralized() {
    PropertyGraphDb database =
        new GdlLoader(new InMemoryGraphDbSupplier(true), GENERALIZED_GDL).get();

    Long collectionId = database.createCollection(0, database.getGraphIds());

    test(database, collectionId, true, GENERALIZED_EXPECTED_SUPPORT);
  }

  @Test
  public void testGeneralizedNoEffect() {
    PropertyGraphDb database = getPredictableDatabase();
    Long collectionId = database.createCollection(0, database.getGraphIds());

    test(database, collectionId, true, PREDICTABLE_EXPECTED_SUPPORT);
  }

  @Test
  public void testGeneralizedDisabled() {
    PropertyGraphDb database =
        new GdlLoader(new InMemoryGraphDbSupplier(true), GENERALIZED_GDL).get();

    Long collectionId = database.createCollection(0, database.getGraphIds());

    test(database, collectionId, false, GENDISABLED_EXPECTED_SUPPORT);
  }

  protected void test(
      PropertyGraphDb database, Long collectionId,
      boolean generalized, Map<String, Long> expectedSupport) {

    StatisticsExtractor<Map<Integer, Long>> extractor =
        new StatisticsBuilder(database, TestConstants.PARALLEL)
            .fromCollection()
            .ofVertexLabels()
            .getSupport(generalized);

    Map<Integer, Long> support = extractor.apply(collectionId);

    for (Map.Entry<Integer, Long> entry : support.entrySet()) {
      String label = database.decode(entry.getKey());

      assertTrue("did not expect " + label + "=" + entry.getValue(),
          expectedSupport.containsKey(label));

      assertEquals("wrong support for " + label,
          expectedSupport.get(label), entry.getValue());
    }

    for (Map.Entry<String, Long> entry : expectedSupport.entrySet()) {
      String label = entry.getKey();

      assertTrue("did not find " + label, support.containsKey(database.encode(label)));
    }
  }
}