package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class MultiDimensionalVector {

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
   *
   * @param dimensionPaths copy of parents' dimension paths
   * @param levels specialized state
   */
  private MultiDimensionalVector(int[][] dimensionPaths, int[] levels) {
    this.dimensionPaths = dimensionPaths;
    this.levels = levels;
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
    if (levels[dimension] >= 0) {
      specialization = new MultiDimensionalVector(dimensionPaths, Arrays.copyOf(levels, levels.length));
      specialization.levels[dimension]--;
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
            if (level >= 0) {
              equal = this.dimensionPaths[dim][level] == that.dimensionPaths[dim][level];
              if (!equal) break;
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
    for (int dim = 0; dim < levels.length; dim++) {
      int level = levels[dim];
      if (level >= 0)
        result = 31 * result + this.dimensionPaths[dim++][level];
    }

    return result;
  }

  /**
   * Factory method to create a vector at its most general state.
   *
   * @param dimensionPaths paths of dimension values from general to special
   */
  public static MultiDimensionalVector create(int[][] dimensionPaths) {
    int size = dimensionPaths.length;
    int[] levels = new int[size];

    for (int i = 0; i < size; i++)
      levels[i] = dimensionPaths[i].length - 1;

    return new MultiDimensionalVector(dimensionPaths, levels);
  }

  @Override
  public String toString() {
    return ArrayUtils.toString(dimensionPaths) + "@" + ArrayUtils.toString(levels);
  }
}
