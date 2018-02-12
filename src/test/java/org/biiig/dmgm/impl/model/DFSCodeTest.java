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

import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.impl.operators.subgraph_mining.DFSCode;
import org.junit.Test;

public class DFSCodeTest extends CachedGraphTestBase {

  @Test
  public void testGetterAndSetter() {
    int lab0 = 0;
    int lab1 = 1;

    CachedGraph graph = new DFSCode(
      0, new int[] {lab0, lab1},
      new int[] {lab0, lab1},
      new int[] {0, 0},
      new int[] {0, 1},
      new boolean[] {true, true}
    );

    test(graph, lab0, lab1);
  }

}