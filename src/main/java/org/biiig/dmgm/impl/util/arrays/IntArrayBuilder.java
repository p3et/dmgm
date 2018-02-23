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

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * Create int[]s of previously unknown size.
 * Internally, an array and the current maximum index are stored.
 * To avoid object instantiations the internal array has an initial size
 * and will be extended only if this size is exceeded.
 * At any time, a copy of the currently filled subarray can be provided.
 * To further avoid instantiations, the builder can be reset.
 * In this case, the internal array is kept and just the maximum index is set to zero.
 */
public class IntArrayBuilder {

  /**
   * An internal array to store values.
   */
  private int[] array;

  /**
   * Length increment in the case of internal array extensions.
   * Additionally, this value is the initial size of the internal array.
   */
  private final int increment;

  /**
   * Current length of the internal array.
   */
  private int length;

  /**
   * Current maximum index of the internal array.
   */
  private int index;

  /**
   * Constructor.
   *
   * @param increment size increment
   */
  public IntArrayBuilder(int increment) {
    this.increment = increment;
    this.array = new int[increment];
    this.length = increment;
    this.index = 0;
  }

  /**
   * Add a value.
   *
   * @param value value
   */
  public void add(int value) {

    // extend internal array if required
    if (!(index < length)) {
      int newLength = length + increment;
      array = Arrays.copyOf(array, newLength);
      length = newLength;
    }

    array[index] = value;
    index++;
  }

  /**
   * Get an array with all elements added so far.
   *
   * @return array
   */
  public int[] get() {
    return ArrayUtils.subarray(array, 0, index);
  }

  /**
   * Logically remove all elements.
   */
  public void reset() {
    index = 0;
  }
}
