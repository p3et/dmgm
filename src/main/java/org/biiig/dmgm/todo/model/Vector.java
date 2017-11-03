package org.biiig.dmgm.todo.model;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.todo.mining.IntArrayUtils;
import org.biiig.dmgm.todo.vector_mining.CrossLevelVectorComparator;

import java.util.Arrays;

/**
 * Created by peet on 13.07.17.
 */
public class Vector implements Comparable<Vector> {
  static CrossLevelVectorComparator comparator = new CrossLevelVectorComparator();

  private int[][] fields;

  public Vector(int[][] fields) {
    this.fields = fields;
  }

  public Vector() {
    this.fields = new int[0][];
  }

  @Override
  public int compareTo(Vector o) {
    return comparator.compare(this.fields, o.fields);
  }

  public Vector deepCopy() {
    return new Vector(IntArrayUtils.deepCopy(fields));
  }

  public int[] getField(int i) {
    return fields[i];
  }

  public int size() {
    return fields.length;
  }

  public void addField(int[] field) {
    this.fields = ArrayUtils.add(fields, field);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Vector vector = (Vector) o;

    return Arrays.deepEquals(fields, vector.fields);
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(fields);
  }

  @Override
  public String toString() {
    return IntArrayUtils.toString(fields);
  }
}
