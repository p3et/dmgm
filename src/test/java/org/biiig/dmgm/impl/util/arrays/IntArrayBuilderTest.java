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

package org.biiig.dmgm.impl.util.arrays;

import org.junit.Test;

import static org.junit.Assert.*;

public class IntArrayBuilderTest {

  private static final int[] ARRAY = new int[] {1, 2, 3};


  @Test
  public void getWithoutIncrement() {
    int increment = 10;
    IntArrayBuilder builder = getBuilder(increment);
    int[] array = builder.get();
    assertArrayEquals("without incrementing", ARRAY, array);
  }


  @Test
  public void getWithIncrement() {
    int increment = 2;
    IntArrayBuilder builder = getBuilder(increment);
    int[] array = builder.get();
    assertArrayEquals("with incrementing", ARRAY, array);
  }

  @Test
  public void reset() {
    int increment = 2;
    IntArrayBuilder builder = getBuilder(increment);
    builder.reset();
    int[] array = builder.get();
    assertEquals("without incrementing", 0, array.length);
  }


  private IntArrayBuilder getBuilder(int increment) {
    IntArrayBuilder builder = new IntArrayBuilder(increment);

    for (int value : ARRAY) {
      builder.add(value);
    }

    return builder;
  }
}