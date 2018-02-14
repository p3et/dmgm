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

package org.biiig.dmgm.impl.operators.fsm.generalized;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.impl.operators.fsm.characteristic.WithEmbeddingAndCategory;
import org.biiig.dmgm.impl.operators.fsm.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.WithGraphId;

import java.util.Arrays;

public class MultiDimensionalVector implements WithGraphId, WithEmbeddingAndCategory {

  private final DFSEmbedding embedding;
  private final int lastSpecialization;

  /**
   * Cached paths of dimension values from general to special.
   * Will not be copied at specializations.
   */
  private final int[][] dimensionPaths;

  /**
   * State, pointing to the current levels of dimension paths.
   * I.e., the actual vector is (dimensionPaths[0][levels[0]], .., dimensionPaths[n][levels[n]])
   */
  private final int[] levels;

  /**
   * Specialization constructor.
   *  @param embedding
   * @param dimensionPaths copy of parents' dimension paths
   * @param levels specialized state
   * @param lastSpecialization
   */
  private MultiDimensionalVector(DFSEmbedding embedding, int[][] dimensionPaths, int[] levels, int lastSpecialization) {
    this.embedding = embedding;
    this.dimensionPaths = dimensionPaths;
    this.levels = levels;
    this.lastSpecialization = lastSpecialization;
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
    if (levels[dimension] + 1 < dimensionPaths[dimension].length) {
      specialization = new MultiDimensionalVector(embedding, dimensionPaths, Arrays.copyOf(levels, levels.length), dimension);
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
            equal = this.dimensionPaths[dim][level] == that.dimensionPaths[dim][level];
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
        result = 31 * result + this.dimensionPaths[dim++][level];
    }

    return result;
  }

  /**
   * Factory method to get a vector at its most general state.
   *
   * @param embedding
   * @param dimensionPaths paths of dimension values from general to special
   */
  public static MultiDimensionalVector create(DFSEmbedding embedding, int[][] dimensionPaths) {
    int size = dimensionPaths.length;
    int[] levels = new int[size];

    for (int i = 0; i < size; i++)
      levels[i] = 0;

    return new MultiDimensionalVector(embedding, dimensionPaths, levels, 0);
  }

  public DFSEmbedding getEmbedding() {
    return embedding;
  }

  public int getLastSpecialization() {
    return lastSpecialization;
  }

  @Override
  public String toString() {
    String[] current = new String[dimensionPaths.length];

    for (int dim = 0; dim < dimensionPaths.length; dim++)
      current[dim] = levels[dim] >= 0 ? String.valueOf(dimensionPaths[dim][levels[dim]]) : "*";

    return
//      ArrayUtils.toString(dimensionPaths) + "@" + ArrayUtils.toString(levels) + "=>" +
        ArrayUtils.toString(current);

  }

  public int getSpecializedValue(int dim) {
    return dimensionPaths[dim][levels[dim]];
  }


  @Override
  public long getGraphId() {
    return embedding.getGraphId();
  }

  public int size() {
    return levels.length;
  }
}
