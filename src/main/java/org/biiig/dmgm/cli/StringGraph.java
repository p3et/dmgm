package org.biiig.dmgm.cli;

import org.apache.commons.lang3.ArrayUtils;

public class StringGraph {

  private String[] vertexLabels;
  private String[] edgeLabels;
  private int[] edgeSources;
  private int[] edgeTargets;

  private String label;

  public StringGraph(String label) {
    this.label = label;
  }

  public void addVertex(String label) {
    vertexLabels = ArrayUtils.add(vertexLabels, label);
  }

  public void addEdge(int source, int target, String label) {
    edgeLabels = ArrayUtils.add(edgeLabels, label);
    edgeSources = ArrayUtils.add(edgeSources, source);
    edgeTargets = ArrayUtils.add(edgeTargets, target);
  }

  @Override
  public String toString() {
    return "Gl:" + label +
      "\n\t" + "Vl=" + ArrayUtils.toString(vertexLabels) +
      "\n\t" + "El=" + ArrayUtils.toString(edgeLabels) +
      "\n\t" + "Es=" + ArrayUtils.toString(edgeSources) +
      "\n\t" + "Et=" + ArrayUtils.toString(edgeTargets);
  }

  public int getVertexCount() {
    return vertexLabels.length;
  }

  public String getVertexLabel(int id) {
    return vertexLabels[id];
  }

  public int getEdgeCount() {
    return edgeLabels.length;
  }

  public String getEdgeLabel(int id) {
    return edgeLabels[id];
  }
}
