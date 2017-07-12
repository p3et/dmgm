package org.biiig.dmgspan.model;

/**
 * Created by peet on 12.07.17.
 */
public class Edge {
  private int source;
  private final int label;
  private int target;
  private int id;

  public Edge(int id, int source, int label, int target) {
    this.id = id;
    this.source = source;
    this.label = label;
    this.target = target;
  }

  public int getSource() {
    return source;
  }

  public int getLabel() {
    return label;
  }

  public int getTarget() {
    return target;
  }

  @Override
  public String toString() {
    return "(" + source + ")-" + label + "->(" + target + ")";
  }

  public boolean isLoop() {
    return source == target;
  }

  public int getId() {
    return id;
  }

  public void setSource(int source) {
    this.source = source;
  }

  public void setTarget(int target) {
    this.target = target;
  }

  public void setId(int id) {
    this.id = id;
  }
}
