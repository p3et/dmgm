package org.biiig.dmgm.impl.model.graph;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.model.graph.DirectedGraph;
import org.biiig.dmgm.todo.gspan.LexicographicDFSCodeComparator;

import java.util.Arrays;

public class DFSCode implements Comparable<DFSCode>, DirectedGraph {

  private final int[] vertexLabels;
  private final int[] edgeMux;
  private final boolean[] directionIndicators;

  private final LexicographicDFSCodeComparator comparator = new LexicographicDFSCodeComparator();

  public DFSCode(int vertexCount, int edgeCount) {
    vertexLabels = new int[vertexCount];
    edgeMux = new int[edgeCount * 3];
    directionIndicators = new boolean[edgeCount];
  }

  public DFSCode(
    int fromTime, int toTime, int fromLabel, boolean outgoing, int edgeLabel, int toLabel) {

    vertexLabels = new int[toTime];

    vertexLabels[fromTime] = fromLabel;
    vertexLabels[toTime] = toLabel;

    edgeMux = new int[] {fromTime, edgeLabel, toTime};

    directionIndicators = new boolean[] {outgoing};
  }

  public DFSCode(int[] vertexLabels, int[] edgeMux, boolean[] directionIndicators) {
    this.vertexLabels = vertexLabels;
    this.edgeMux = edgeMux;
    this.directionIndicators = directionIndicators;
  }



  @Override
  public int[] getVertexData(int vertexId) {
    return new int[] {vertexLabels[vertexId]};
  }

  @Override
  public void setVertex(int vertexId, int label) {
    vertexLabels[vertexId] = label;
  }

  @Override
  public void setVertex(int vertexId, int[] labels) {
    if (labels.length != 1) {
      throw new IllegalArgumentException(
        "A vertex must exactly have 1 data field (label) but has " + ArrayUtils.toString(labels));
    } else {
      setVertex(vertexId, labels[0]);
    }
  }

  public int getVertexLabel(int vertexTime) {
    return vertexLabels[vertexTime];
  }

  @Override
  public int[] getEdgeData(int edgeId) {
    return new int[] {getEdgeLabel(edgeId)};
  }

  @Override
  public void setEdge(int edgeId, int sourceId, int targetId, int label) {
    setFromTime(edgeId, sourceId);
    setEdgeLabel(edgeId, label);
    setToTime(edgeId, targetId);
    directionIndicators[edgeId] = true;
  }

  @Override
  public void setEdge(int edgeId, int sourceId, int targetId, int[] labels) {

    if (labels.length != 1) {
      throw new IllegalArgumentException(
        "An edge must exactly have 1 data field (label) but has " + ArrayUtils.toString(labels));
    } else {
      setEdge(edgeId, sourceId, targetId, labels[0]);
    }
  }

  public void setVertexLabel(int vertexTime, int label) {
    vertexLabels[vertexTime] = label;
  }

  private void setEdgeLabel(int edgeTime, int label) {
    edgeMux[getEdgeLabelMuxIndex(edgeTime)] = label;
  }

  private void setFromTime(int edgeId, int sourceId) {
    edgeMux[getFromMuxIndex(edgeId)] = sourceId;
  }

  private void setToTime(int edgeId, int toId) {
    edgeMux[getToMuxIndex(edgeId)] = toId;
  }

  @Override
  public int getSourceId(int edgeTime) {
    return isOutgoing(edgeTime) ? getFromTime(edgeTime) : getToTime(edgeTime);
  }

  @Override
  public int getTargetId(int edgeTime) {
    return isOutgoing(edgeTime) ? getToTime(edgeTime) : getFromTime(edgeTime);
  }

  @Override
  public int[] getOutgoingEdgeIds(int vertexId) {
    return getIncidentEdgeIds(vertexId, true);
  }

  @Override
  public int[] getIncomingEdgeIds(int vertexId) {
    return getIncidentEdgeIds(vertexId, false);
  }

  private int[] getIncidentEdgeIds(int vertexId, boolean outgoing) {
    int i = 0;
    int edgeCount = getEdgeCount();
    int[] edgeIds = new int[edgeCount];

    for (int edgeId = 0; edgeId < edgeCount; edgeId++) {
      int currentId = outgoing ? getSourceId(edgeId) : getTargetId(edgeId);
      if (currentId == vertexId) {
        edgeIds[i] = edgeId;
        i++;
      }
    }

    if (i != edgeCount) {
      if (i == 0) {
        edgeIds = new int[0];
      } else {
        edgeIds = ArrayUtils.subarray(edgeIds, 0, i);
      }
    }

    return edgeIds;
  }

  @Override
  public int getVertexCount() {
    return vertexLabels.length;
  }

  @Override
  public int getEdgeCount() {
    return directionIndicators.length;
  }

  public int getFromTime(int edgeTime) {
    return edgeMux[getFromMuxIndex(edgeTime)];
  }

  public int getToTime(int edgeTime) {
    return edgeMux[getToMuxIndex(edgeTime)];
  }

  public boolean isOutgoing(int edgeTime) {
    return directionIndicators[edgeTime];
  }

  public int getEdgeLabel(int edgeTime) {
    return edgeMux[getEdgeLabelMuxIndex(edgeTime)];
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DFSCode that = (DFSCode) o;

    return Arrays.equals(this.vertexLabels, that.vertexLabels) &&
      Arrays.equals(this.edgeMux, that.edgeMux) &&
      Arrays.equals(this.directionIndicators, that.directionIndicators);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    for (int edgeTime = 0; edgeTime < getEdgeCount(); edgeTime++) {
      boolean outgoing = isOutgoing(edgeTime);
      int fromTime = getFromTime(edgeTime);
      int toTime = getToTime(edgeTime);
      builder
        .append(fromTime)
        .append(":")
        .append(getVertexLabel(fromTime))
        .append(outgoing ? "-" : "<-")
        .append(getEdgeLabel(edgeTime))
        .append(outgoing ? "->" : "-")
        .append(toTime)
        .append(":")
        .append(getVertexLabel(toTime))
        .append(" ");
    }

    return builder.toString();
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(vertexLabels) *
      Arrays.hashCode(edgeMux) *
      Arrays.hashCode(directionIndicators);
  }

  @Override
  public int compareTo(DFSCode that) {
    return comparator.compare(this, that);
  }

  public DFSCode growChild(int fromTime, int toTime, boolean outgoing, int edgeLabel, int toLabel) {

    // copy and extend vertex structure
    boolean forwards = toTime > fromTime;
    int newVertexCount = getVertexCount() + (forwards ? 1 : 0);
    int[] vertexLabelsCopy = Arrays.copyOf(vertexLabels, newVertexCount);

    // copy and extend edge structures
    int edgeTime = getEdgeCount();
    int newEdgeCount = edgeTime + 1;

    int[] edgeMuxCopy = Arrays.copyOf(edgeMux, newEdgeCount);
    boolean[] directionIndicatorsCopy = Arrays.copyOf(this.directionIndicators, newEdgeCount);

    // create child
    DFSCode child = new DFSCode(vertexLabelsCopy, edgeMuxCopy, directionIndicatorsCopy);

    // set data of extension
    if (forwards) {
      child.setVertexLabel(toTime, toLabel);
    }

    child.setFromTime(edgeTime, fromTime);
    child.setEdgeLabel(edgeTime, edgeLabel);
    child.setToTime(edgeTime, toTime);
    child.setDirectionIndicator(edgeTime, outgoing);

    return child;
  }

  private void setDirectionIndicator(int edgeTime, boolean outgoing) {
    directionIndicators[edgeTime] = outgoing;
  }

  public boolean parentOf(DFSCode child) {
    boolean parent = this.getEdgeCount() <= child.getEdgeCount();

    if (parent) {
      for (int vertexTime = 0; vertexTime < this.getVertexCount(); vertexTime++) {
        parent = this.getVertexLabel(vertexTime) == child.getVertexLabel(vertexTime);

        if (!parent) {
          break;
        }
      }
      for (int edgeTime = 0; edgeTime < this.getEdgeCount(); edgeTime++) {
        parent = this.getFromTime(edgeTime) == child.getFromTime(edgeTime) &&
          this.getToTime(edgeTime) == child.getToTime(edgeTime) &&
          this.isOutgoing(edgeTime) == child.isOutgoing(edgeTime) &&
          this.getEdgeLabel(edgeTime) == child.getEdgeLabel(edgeTime);

        if (!parent) {
          break;
        }
      }
    }

    return parent;
  }

  public DFSCode deepCopy() {
    int[] vertexLabelsCopy = Arrays.copyOf(vertexLabels, getVertexCount());
    int[] edgeMuxCopy = Arrays.copyOf(edgeMux, getEdgeCount());
    boolean[] directionIndicatorsCopy = Arrays.copyOf(this.directionIndicators, getEdgeCount());

    return new DFSCode(vertexLabelsCopy, edgeMuxCopy, directionIndicatorsCopy);
  }

  private int getFromMuxIndex(int edgeTime) {
    return edgeTime * 3;
  }

  private int getEdgeLabelMuxIndex(int edgeTime) {
    return getFromMuxIndex(edgeTime) + 1;
  }

  private int getToMuxIndex(int edgeTime) {
    return getFromMuxIndex(edgeTime) + 2;
  }



}
