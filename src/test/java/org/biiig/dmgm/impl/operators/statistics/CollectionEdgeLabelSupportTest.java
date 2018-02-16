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

public class CollectionEdgeLabelSupportTest extends DmgmTestBase {

  private static final String GDL = "[()-[:a]->()-[:b]->()][()-[:a]->()-[:a]->()]";

  private static final Map<String, Long> EXPECTED_SUPPORT = Maps.newHashMap();

  static {
    EXPECTED_SUPPORT.put("a", 2L);
    EXPECTED_SUPPORT.put("b", 1L);
  }

  @Test
  public void testSimple() {
    PropertyGraphDb database =
        new GdlLoader(new InMemoryGraphDbSupplier(TestConstants.PARALLEL), GDL).get();

    Long collectionId = database.createCollection(0, database.getGraphIds());

    StatisticsExtractor<Map<Integer, Long>> extractor =
        new StatisticsBuilder(database, TestConstants.PARALLEL)
            .fromCollection()
            .ofEdgeLabels()
            .getSupport();

    Map<Integer, Long> support = extractor.apply(collectionId);

    for (Map.Entry<Integer, Long> entry : support.entrySet()) {
      String label = database.decode(entry.getKey());

      assertTrue("did not expect " + label + "=" + entry.getValue(),
          EXPECTED_SUPPORT.containsKey(label));

      assertEquals("wrong support for " + label,
          EXPECTED_SUPPORT.get(label), entry.getValue());
    }

    for (Map.Entry<String, Long> entry : EXPECTED_SUPPORT.entrySet()) {
      String label = entry.getKey();

      assertTrue("did not find " + label, support.containsKey(database.encode(label)));
    }
  }
}