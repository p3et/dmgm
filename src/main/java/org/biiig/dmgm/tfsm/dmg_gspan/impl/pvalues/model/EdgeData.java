package org.biiig.dmgm.tfsm.dmg_gspan.impl.pvalues.model;

/**
 * (sourceId, targetId, sourceLabel, targetLabel, edgeLabel, multiplicity)
 */
public class EdgeData implements Comparable<EdgeData> {

  private final int graphId;
  private final int sourceId;
  private final int targetId;
  private final String sourceLabel;
  private String[] edgeLabels;
  private final String targetLabel;

  public EdgeData(int graphId, int sourceId, int targetId, String sourceLabel, String edgeLabel,
    String targetLabel) {
    this.graphId = graphId;
    this.sourceId = sourceId;
    this.targetId = targetId;
    this.sourceLabel = sourceLabel;
    this.edgeLabels = new String[] {edgeLabel};
    this.targetLabel = targetLabel;
  }

  public int getSourceId() {
    return this.sourceId;
  }

  public int getTargetId() {
    return this.targetId;
  }

  public String getSourceLabel() {
    return this.sourceLabel;
  }

  public int getMultiplicity() {
    return this.edgeLabels.length;
  }

  public String getTargetLabel() {
    return this.targetLabel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EdgeData edgeData = (EdgeData) o;

    if (graphId != edgeData.graphId) {
      return false;
    }
    if (sourceId != edgeData.sourceId) {
      return false;
    }
    return targetId == edgeData.targetId;
  }

  @Override
  public int hashCode() {
    int result = graphId;
    result = 31 * result + sourceId;
    result = 31 * result + targetId;
    return result;
  }

  @Override
  public int compareTo(EdgeData that) {
    int comparison = this.graphId - that.graphId;

    if (comparison == 0) {
      comparison = this.sourceId - that.sourceId;

      if (comparison == 0) {
        comparison = this.targetId - that.targetId;
      }
    }

    return comparison;
  }

  public void setEdgeLabels(String[] edgeLabels) {
    this.edgeLabels = edgeLabels;
  }

  public String[] getEdgeLabels() {
    return edgeLabels;
  }
}
