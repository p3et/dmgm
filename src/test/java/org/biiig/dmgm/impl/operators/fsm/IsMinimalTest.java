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

package org.biiig.dmgm.impl.operators.fsm;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class IsMinimalTest {

  @Test
  public void test1() {

    // 0:0-3->1:1 0:0<-3-2:2

    DfsCode dfsCode = new DfsCode(
      0, new int[] {0 ,1 ,2},
      new int[] {3, 3},
      new int[] {0, 2},
      new int[] { 1, 0},
      new boolean[] {true, false});

    assertTrue(new IsMinimal().test(dfsCode));
  }
}