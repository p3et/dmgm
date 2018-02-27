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

import java.util.Arrays;

/**
 * An array wrapper that ensures set properties.
 */
public class ImmutableIntSet {

  /**
   * Internal array to store values.
   */
  private final int[] values;

  /**
   * Constructor.
   *
   * @param values potentially non-distinct values
   */
  public ImmutableIntSet(int[] values) {
    this.values = DmgmArrayUtils.distinct(values);
  }

  /**
   * Check, if the set contains a value.
   *
   * @param value value
   * @return true <=> contained
   */
  public boolean contains(int value) {
    return Arrays.binarySearch(values, value) >= 0;
  }

}
