package org.biiig.dmgm.todo.vector_mining;

import java.io.Serializable;
import java.util.Comparator;

public class CrossLevelVectorComparator 
  implements Comparator<int[][]>, Serializable {

  private final CrossLevelDimensionComparator fieldComparator = new CrossLevelDimensionComparator();

  @Override
  public int compare(int[][] a, int[][] b) {

    int comparison = a.length - b.length;

    if (comparison == 0) {
      for (int i = 0; i < a.length; i++) {

        comparison = fieldComparator.compare(a[i], b[i]);

        if (comparison != 0) {
          break;
        }
      }
    } else {
      throw new IllegalArgumentException("Vectors must have the same number of dimensions.");
    }

    return comparison;
  }
}
