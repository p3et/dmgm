package org.biiig.dmgm.impl.graph;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.Graph;

import java.util.Arrays;

public class GraphBase implements Graph {
  private int label = -1;
  private int id = -1;
  protected int[] vertexLabels = new int[0];
  protected int[] edgeLabels = new int[0];
  protected int[] sourceIds = new int[0];
  protected int[] targetIds = new int[0];

  @Override
  public String toString() {
    return "g=" +
      "\nV=" + Arrays.toString(vertexLabels) +
      "\nE=" + Arrays.toString(edgeLabels) +
      "\nS=" + Arrays.toString(sourceIds) +
      "\nT=" + Arrays.toString(targetIds);
  }

  @Override
  public int getLabel() {
    return label;
  }

  @Override
  public void setLabel(int label) {
    this.label = label;
  }

  @Override
  public int getVertexCount() {
    return vertexLabels.length;
  }

  @Override
  public int getEdgeCount() {
    return edgeLabels.length;
  }

  @Override
  public void addVertex(int label) {
    vertexLabels = ArrayUtils.add(vertexLabels, label);
  }

  @Override
  public void addEdge(int sourceId, int targetId, int label) {
    edgeLabels = ArrayUtils.add(edgeLabels, label);
    sourceIds = ArrayUtils.add(sourceIds, sourceId);
    targetIds = ArrayUtils.add(targetIds, targetId);
  }

  @Override
  public int getVertexLabel(int vertexId) {
    return vertexLabels[vertexId];
  }

  @Override
  public int getEdgeLabel(int edgeId) {
    return edgeLabels[edgeId];
  }

  @Override
  public int getSourceId(int edgeId) {
    return sourceIds[edgeId];
  }

  @Override
  public int getTargetId(int edgeId) {
    return targetIds[edgeId];
  }

  @Override
  public int[] getOutgoingEdgeIds(int vertexId) {
    int[] edgeIds = new int[0];

    for (int edgeId = 0; edgeId < getEdgeCount(); edgeId++)
      if (sourceIds[edgeId] == vertexId)
        edgeIds = ArrayUtils.add(edgeIds, edgeId);

    return edgeIds;
  }

  @Override
  public int[] getIncomingEdgeIds(int vertexId) {
    int[] edgeIds = new int[0];

    for (int edgeId = 0; edgeId < getEdgeCount(); edgeId++)
      if (targetIds[edgeId] == vertexId)
        edgeIds = ArrayUtils.add(edgeIds, edgeId);

    return edgeIds;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

}
