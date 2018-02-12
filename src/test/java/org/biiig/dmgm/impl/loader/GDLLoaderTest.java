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


package org.biiig.dmgm.impl.loader;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.model.CachedGraph;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test reading a GDL string.
 */
public class GDLLoaderTest extends DMGMTestBase {

  @Test
  public void testSimpleGraph() {
    String gdl = "g1:G[(v1:A)-[:a]->(v1)-[:b]->(:B)], g2:G[(v1:A)-[:a]->(v1)-[:c]->(:C)]";

    PropertyGraphDB db = new GDLLoader(DB_FACTORY, gdl).get();

    int graphLabel = db.encode("G");
    int colLabel = db.encode("COL");

    long[] graphIds = db.queryElements(i -> i == graphLabel);
    Long colId = db.createCollection(colLabel, graphIds);
    List<CachedGraph> graphCollection = db.getCachedCollection(colId);

    assertEquals("model count", 2, graphCollection.size());

    System.out.println(db);
  }

}