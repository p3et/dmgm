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

package org.biiig.dmgm.impl.operators.patternmining;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;


/**
 * A vector whose fields (dimensions) can be specialized.
 *
 * @see <a href="http://ieeexplore.ieee.org/document/8244685/">Generalized Subgraph Mining</a>
 */
public class SpecializableVector implements WithEmbedding {

  /**
   * The graph embedding of this vector.
   */
  private final DfsEmbedding embedding;

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
   * @param lastSpecialization last specialized field
   */
  private SpecializableVector(
      DfsEmbedding embedding, int[][] taxonomyPaths, int[] levels, int lastSpecialization) {
    this.embedding = embedding;
    this.taxonomyPaths = taxonomyPaths;
    this.levels = levels;
    this.lastSpecialization = lastSpecialization;
  }

  /**
   * Factory method to get a vector at its most general state.
   *
   * @param embedding embedding of the vector
   * @param taxonomyPaths paths of dimension values from general to special
   */
  public static SpecializableVector create(DfsEmbedding embedding, int[][] taxonomyPaths) {
    int size = taxonomyPaths.length;
    int[] levels = new int[size];

    for (int i = 0; i < size; i++) {
      levels[i] = 0;
    }

    return new SpecializableVector(embedding, taxonomyPaths, levels, 0);
  }

  /**
   * Return a specialization of the current state, if possible.
   *
   * @param dimension dimension to specialize
   * @return specialization or null if not possible
   */
  public SpecializableVector getSpecialization(int dimension) {

    SpecializableVector specialization = null;

    // dimension c
    if (levels[dimension] + 1 < taxonomyPaths[dimension].length) {
      specialization = new SpecializableVector(
          embedding, taxonomyPaths, Arrays.copyOf(levels, levels.length), dimension);
      specialization.levels[dimension]++;
    }

    return specialization;
  }

  /**
   * Get the current value of a specific field.
   *
   * @param field field index
   * @return value
   */
  public int getSpecializedValue(int field) {
    return taxonomyPaths[field][levels[field]];
  }

  @Override
  public boolean equals(Object o) {
    boolean equal = this == o;

    if (!equal) {
      equal = o != null && getClass() == o.getClass();

      if (equal) {
        SpecializableVector that = (SpecializableVector) o;

        // first, check size and specialization state
        equal = Arrays.equals(this.levels, that.levels);

        // if equal, check values of current state
        if (equal) {
          for (int field = 0; field < levels.length; field++) {
            int level = levels[field];
            equal = this.taxonomyPaths[field][level] == that.taxonomyPaths[field][level];
            if (!equal) {
              break;
            }
          }
        }
      }
    }

    return equal;
  }

  @Override
  public int hashCode() {
    int result = 1;
    for (int field = 0; field < levels.length; field++) {
      int level = levels[field];
      if (level >= 0) {
        result = 31 * result + this.taxonomyPaths[field++][level];
      }
    }

    return result;
  }

  @Override
  public String toString() {
    String[] current = new String[taxonomyPaths.length];

    for (int field = 0; field < taxonomyPaths.length; field++) {
      current[field] = levels[field] >= 0
          ? String.valueOf(taxonomyPaths[field][levels[field]])
          : "*";
    }

    return ArrayUtils.toString(taxonomyPaths)
        + "@"
        + ArrayUtils.toString(levels)
        + "=>"
        + ArrayUtils.toString(current);
  }


  // GETTER

  public DfsEmbedding getEmbedding() {
    return embedding;
  }

  public int getLastSpecialization() {
    return lastSpecialization;
  }

  public int size() {
    return levels.length;
  }
}
