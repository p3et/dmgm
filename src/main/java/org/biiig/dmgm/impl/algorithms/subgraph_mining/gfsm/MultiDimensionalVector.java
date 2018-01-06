package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.GraphInformation;

import java.util.Arrays;
import java.util.Optional;

public class MultiDimensionalVector implements GraphInformation {

  public static final int ARBITRARY = -1;

  private final int graphId;
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
   *
   * @param graphId
   * @param dimensionPaths copy of parents' dimension paths
   * @param levels specialized state
   * @param lastSpecialization
   */
  private MultiDimensionalVector(int graphId, int[][] dimensionPaths, int[] levels, int lastSpecialization) {
    this.graphId = graphId;
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
      specialization = new MultiDimensionalVector(graphId, dimensionPaths, Arrays.copyOf(levels, levels.length), dimension);
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
   * @param graphId
   * @param dimensionPaths paths of dimension values from general to special
   */
  public static MultiDimensionalVector create(int graphId, int[][] dimensionPaths) {
    int size = dimensionPaths.length;
    int[] levels = new int[size];

    for (int i = 0; i < size; i++)
      levels[i] = ARBITRARY;

    return new MultiDimensionalVector(graphId, dimensionPaths, levels, 0);
  }

  public int getGraphId() {
    return graphId;
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

  public Optional<Integer> getSpecializedValue(int dim) {
    return levels[dim] >= 0 ? Optional.of(dimensionPaths[dim][levels[dim]]) : Optional.empty();
  }


}
