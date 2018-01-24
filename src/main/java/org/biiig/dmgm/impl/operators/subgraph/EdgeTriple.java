package org.biiig.dmgm.impl.operators.subgraph;

public class EdgeTriple {
  private int sourceId;
  private int targetId;
  private int edgeLabel;

  public EdgeTriple(int sourceId, int targetId, int edgeLabel) {
    this.sourceId = sourceId;
    this.targetId = targetId;
    this.edgeLabel = edgeLabel;
  }

  public int getSourceId() {
    return sourceId;
  }

  public void setSourceId(int sourceId) {
    this.sourceId = sourceId;
  }

  public int getTargetId() {
    return targetId;
  }

  public void setTargetId(int targetId) {
    this.targetId = targetId;
  }

  public int getEdgeLabel() {
    return edgeLabel;
  }

  public void setEdgeLabel(int edgeLabel) {
    this.edgeLabel = edgeLabel;
  }
}
