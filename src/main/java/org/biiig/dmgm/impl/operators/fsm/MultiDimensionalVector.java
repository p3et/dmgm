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

package org.biiig.dmgm.impl.operators.fsm;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class MultiDimensionalVector implements WithDFSEmbedding {

  /**
   * The graph embedding of this vector.
   */
  private final DFSEmbedding embedding;

  /**
   * The dimension that was specialized the last time.
   */
  private final int lastSpecialization;

  /**
   * Cached paths of dimension values from general to special.
   * Will not be copied at specializations.
   */
  private final int[][] taxonomyPaths;

  /**
   * State, pointing to the current levels of dimension paths.
   * I.e., the actual vector is (dimensionPaths[0][levels[0]], .., dimensionPaths[n][levels[n]])
   */
  private final int[] levels;

  /**
   * Specialization constructor.
   * @param embedding embedding of the vector
   * @param taxonomyPaths copy of parents' dimension paths
   * @param levels specialized state
   * @param lastSpecialization
   */
  private MultiDimensionalVector(DFSEmbedding embedding, int[][] taxonomyPaths, int[] levels, int lastSpecialization) {
    this.embedding = embedding;
    this.taxonomyPaths = taxonomyPaths;
    this.levels = levels;
    this.lastSpecialization = lastSpecialization;
  }

  /**
   * Factory method to get a vector at its most general state.
   *
   * @param embedding
   * @param taxonomyPaths paths of dimension values from general to special
   */
  public static MultiDimensionalVector create(DFSEmbedding embedding, int[][] taxonomyPaths) {
    int size = taxonomyPaths.length;
    int[] levels = new int[size];

    for (int i = 0; i < size; i++)
      levels[i] = 0;

    return new MultiDimensionalVector(embedding, taxonomyPaths, levels, 0);
  }


  /**
   * Return a specialization of the current state, if possible.
   *
   * @param dimension dimension to specialize
   * @return specialization or null if not possible
   */
  public MultiDimensionalVector getSpecialization(int dimension) {

    MultiDimensionalVector specialization = null;

    // dimension c
    if (levels[dimension] + 1 < taxonomyPaths[dimension].length) {
      specialization = new MultiDimensionalVector(embedding, taxonomyPaths, Arrays.copyOf(levels, levels.length), dimension);
      specialization.levels[dimension]++;
    }

    return specialization;
  }

  @Override
  public boolean equals(Object o) {
    boolean equal = this == o;

    if (!equal) {
      equal = o != null && getClass() == o.getClass();

      if (equal) {
        MultiDimensionalVector that = (MultiDimensionalVector) o;

        // first, check size and specialization state
        equal = Arrays.equals(this.levels, that.levels);

        // if equal, check values of current state
        if (equal) {
          for (int dim = 0; dim < levels.length; dim++) {
            int level = levels[dim];
            equal = this.taxonomyPaths[dim][level] == that.taxonomyPaths[dim][level];
            if (!equal) break;
          }
        }
      }
    }

    return equal;
  }

  @Override
  public int hashCode() {
    int result = 1;
    for (int dim = 0; dim < levels.length; dim++) {
      int level = levels[dim];
      if (level >= 0)
        result = 31 * result + this.taxonomyPaths[dim++][level];
    }

    return result;
  }

  public DFSEmbedding getEmbedding() {
    return embedding;
  }

  public int getLastSpecialization() {
    return lastSpecialization;
  }

  @Override
  public String toString() {
    String[] current = new String[taxonomyPaths.length];

    for (int dim = 0; dim < taxonomyPaths.length; dim++)
      current[dim] = levels[dim] >= 0 ? String.valueOf(taxonomyPaths[dim][levels[dim]]) : "*";

    return
//      ArrayUtils.toString(dimensionPaths) + "@" + ArrayUtils.toString(levels) + "=>" +
        ArrayUtils.toString(current);

  }

  public int getSpecializedValue(int dim) {
    return taxonomyPaths[dim][levels[dim]];
  }

  public int size() {
    return levels.length;
  }
}
