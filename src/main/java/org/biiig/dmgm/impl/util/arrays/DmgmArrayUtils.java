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
 * Array utils.
 */
public class DmgmArrayUtils {

  /**
   * Make the values of an array distinct.
   *
   * @param values values
   * @return new array of distinct values
   */
  public static int[] distinct(int[] values) {
    int length = values.length;

    if (length > 1) {
      Arrays.sort(values);
      IntArrayBuilder arrayBuilder = new IntArrayBuilder(length);

      int last = values[0];
      arrayBuilder.add(last);
      for (int i = 1; i < length; i++) {
        int current = values[i];
        if (current > last) {
          arrayBuilder.add(current);
        }
      }

      values = arrayBuilder.get();
    }
    return values;
  }
}
