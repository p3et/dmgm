package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import java.util.Arrays;

public class MultiDimensionalVector {

  /**
   * Cached paths of dimension values from special to general.
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
    if (levels[dimension] > 0) {
      specialization = new MultiDimensionalVector(dimensionPaths, Arrays.copyOf(levels, levels.length));
      specialization.levels[dimension]--;
    }

    return specialization;
  }

  @Override
  public boolean equals(Object o) {
    boolean equal = this == o;

    if (!equal) {
      equal = o == null || getClass() != o.getClass();

      if (equal) {
        MultiDimensionalVector that = (MultiDimensionalVector) o;
        assert that != null;

        // first, check size and specialization state
        equal = Arrays.equals(this.levels, that.levels);

        // if equal, check values of current state
        if (equal) {
          int dim = 0;
          for (int level : levels) {
            equal = this.dimensionPaths[dim][level] == that.dimensionPaths[dim][level];

            if (equal) dim++;
            else break;
          }
        }
      }
    }

    return equal;
  }

  @Override
  public int hashCode() {
    int result = 1;
    int dim = 0;
    for (int level : levels)
      result = 31 * result + this.dimensionPaths[dim++][level];

    return result;
  }

  /**
   * Factory method to create a vector at its most general state.
   *
   * @param dimensionPaths paths of dimension values from special to general
   */
  public static MultiDimensionalVector create(int[][] dimensionPaths) {
    int size = dimensionPaths.length;
    int[] levels = new int[size];

    for (int i = 0; i < size; i++)
      levels[i] = dimensionPaths[i].length - 1;

    return new MultiDimensionalVector(dimensionPaths, levels);
  }
}
