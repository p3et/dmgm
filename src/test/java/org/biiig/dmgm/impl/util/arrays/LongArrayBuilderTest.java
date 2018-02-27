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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class LongArrayBuilderTest {

  private static final long[] ARRAY = new long[] {1, 2, 3};


  @Test
  public void getWithoutIncrement() {
    int increment = 10;
    LongArrayBuilder builder = getBuilder(increment);
    long[] array = builder.get();
    assertArrayEquals("without incrementing", ARRAY, array);
  }


  @Test
  public void getWithIncrement() {
    int increment = 2;
    LongArrayBuilder builder = getBuilder(increment);
    long[] array = builder.get();
    assertArrayEquals("with incrementing", ARRAY, array);
  }

  @Test
  public void reset() {
    int increment = 2;
    LongArrayBuilder builder = getBuilder(increment);
    builder.reset();
    long[] array = builder.get();
    assertEquals("without incrementing", 0, array.length);
  }


  private LongArrayBuilder getBuilder(int increment) {
    LongArrayBuilder builder = new LongArrayBuilder(increment);

    for (long value : ARRAY) {
      builder.add(value);
    }

    return builder;
  }
}