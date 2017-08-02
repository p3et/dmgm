package org.biiig.dmgm.todo.vector_mining;

import org.biiig.dmgm.todo.mining.IntArrayUtils;
import org.biiig.dmgm.todo.model.Vector;

public class GFSMLatticeNode implements Comparable<GFSMLatticeNode> {

  private Vector vector;
  private int minField;
  private int support;
  private int frequency;
  private int[] supporters;

  public GFSMLatticeNode() {
  }

  public GFSMLatticeNode(
    Vector vector, int minField,  int support, int frequency,int[] supporters) {
    this.vector = vector.deepCopy();
    this.minField = minField;
    this.support = support;
    this.frequency = frequency;
    this.supporters = supporters;
  }

  public Vector getVector() {
    return vector;
  }

  public int getMinField() {
    return minField;
  }

  public void setMinField(int minField) {
    this.minField = minField;
  }

  public int getSupport() {
    return support;
  }

  public void setSupport(int support) {
    this.support = support;
  }

  public int getFrequency() {
    return frequency;
  }

  public void setFrequency(int frequency) {
    this.frequency = frequency;
  }
  
  public int[] getSupporters() {
    return supporters;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder()
      .append(vector)
      .append("|i:")
      .append(getMinField())
      .append("|s:")
      .append(getSupport())
      .append("|f:")
      .append(getFrequency())
      .append("|v:")
      .append(IntArrayUtils.toString(supporters));


    return builder.toString();
  }

  @Override
  public int compareTo(GFSMLatticeNode that) {
    return this.vector.compareTo(that.vector);
  }

  public GFSMLatticeNode deepCopy() {
    return new GFSMLatticeNode(vector.deepCopy(), minField, support, frequency, supporters.clone());
  }

  public void setSupporters(int[] supporters) {
    this.supporters = supporters;
  }

  public void setVector(Vector vector) {
    this.vector = vector;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    GFSMLatticeNode that = (GFSMLatticeNode) o;

    return vector != null ? vector.equals(that.vector) : that.vector == null;
  }

  @Override
  public int hashCode() {
    return vector != null ? vector.hashCode() : 0;
  }
}
