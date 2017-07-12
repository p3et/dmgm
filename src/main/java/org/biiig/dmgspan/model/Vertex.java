package org.biiig.dmgspan.model;

public class Vertex implements Comparable<Vertex> {
  private int id;
  private final int label;

  public Vertex(int id, int label) {
    this.id = id;
    this.label = label;
  }

  public int getId() {
    return id;
  }

  public int getLabel() {
    return label;
  }

  @Override
  public String toString() {
    return "(" + label + ":" + id + ")";
  }


  @Override
  public int compareTo(Vertex that) {
    return this.label - that.label;
  }

  public void setId(int id) {
    this.id = id;
  }
}
