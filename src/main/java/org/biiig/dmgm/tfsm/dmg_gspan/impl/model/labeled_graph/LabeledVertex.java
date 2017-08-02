package org.biiig.dmgm.tfsm.dmg_gspan.impl.model.labeled_graph;

public class LabeledVertex implements Comparable<LabeledVertex> {
  private int id;
  private final int label;

  public LabeledVertex(int id, int label) {
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
    return "(" + id + ":" + label + ")";
  }


  @Override
  public int compareTo(LabeledVertex that) {
    return this.label - that.label;
  }

  public void setId(int id) {
    this.id = id;
  }
}
