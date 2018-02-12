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

package org.biiig.dmgm.impl.model;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.model.CachedGraph;

import java.util.Objects;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;

/**
 * Created by peet on 02.08.17.
 */
public abstract class CachedGraphTestBase extends DMGMTestBase {

  protected void test(CachedGraph graph, int lab0, int lab1) {
    assertEquals("vertex count", 2, graph.getVertexCount());
    assertEquals("vertex format 0", lab0, graph.getVertexLabel(0));
    assertEquals("vertex format 1", lab1, graph.getVertexLabel(1));
    assertEquals("edge count", 2, graph.getEdgeCount());
    assertEquals("edge format 0", lab0, graph.getEdgeLabel(0));
    assertEquals("edge format 1", lab1, graph.getEdgeLabel(1));
    assertEquals("edge source 0", 0, graph.getSourceId(0));
    assertEquals("edge target 0", 0, graph.getTargetId(0));
    assertEquals("edge source 1", 0, graph.getSourceId(1));
    assertEquals("edge target 1", 1, graph.getTargetId(1));

    int[] out = graph.getOutgoingEdgeIds(0);
    int[] expA = new int[] {0, 1};
    int[] expB = new int[] {1, 0};

    assertTrue("ougoing edges 0",
      Objects.deepEquals(out, expA) || Objects.deepEquals(out, expB));

    assertArrayEquals("incoming edges 0", new int[] {0}, graph.getIncomingEdgeIds(0));
    assertArrayEquals("ougoing edges 1", new int[0], graph.getOutgoingEdgeIds(1));
    assertArrayEquals("incoming edges 1", new int[] {1}, graph.getIncomingEdgeIds(1));
  }
}
