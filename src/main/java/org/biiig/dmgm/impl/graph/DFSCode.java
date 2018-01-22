package org.biiig.dmgm.impl.graph;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class DFSCode extends GraphBase implements Comparable<DFSCode> {

  private boolean[] outgoings;

  private final DFSCodeComparator comparator = new DFSCodeComparator();
  private int[] rightmostPath;

  public DFSCode() {
    super();
  }

  public void addEdge(int fromTime, int toTime, int label, boolean outgoing) {

    edgeLabels = ArrayUtils.add(edgeLabels, label);
    outgoings = ArrayUtils.add(outgoings, outgoing);

    if (outgoing) {
      sourceIds = ArrayUtils.add(sourceIds, fromTime);
      targetIds = ArrayUtils.add(targetIds, toTime);
    } else {
      sourceIds = ArrayUtils.add(sourceIds, toTime);
      targetIds = ArrayUtils.add(targetIds, fromTime);
    }
  }

  @Override
  public int addEdge(int sourceId, int targetId, int label) {
    addEdge(sourceId, targetId, label, true);
    return sourceId;
  }

  public int getFromTime(int edgeTime) {
    return outgoings[edgeTime] ? sourceIds[edgeTime] : targetIds[edgeTime];
  }

  public int getToTime(int edgeTime) {
    return outgoings[edgeTime] ? targetIds[edgeTime] : sourceIds[edgeTime];
  }

  public boolean isOutgoing(int edgeTime) {
    return outgoings[edgeTime];
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

    return
      Arrays.equals(this.outgoings, that.outgoings) &&
      Arrays.equals(this.vertexLabels, that.vertexLabels) &&
      Arrays.equals(this.edgeLabels, that.edgeLabels) &&
      Arrays.equals(this.sourceIds, that.sourceIds) &&
      Arrays.equals(this.targetIds, that.targetIds);
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
      Arrays.hashCode(edgeLabels) *
      Arrays.hashCode(sourceIds) *
      Arrays.hashCode(targetIds) *
      Arrays.hashCode(outgoings);
  }

  @Override
  public int compareTo(DFSCode that) {
    return comparator.compare(this, that);
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
    DFSCode copy = new DFSCode();

    copy.vertexLabels = Arrays.copyOf(vertexLabels, vertexLabels.length);

    int edgeCount = edgeLabels.length;
    copy.edgeLabels = Arrays.copyOf(edgeLabels, edgeCount);
    copy.sourceIds = Arrays.copyOf(sourceIds, edgeCount);
    copy.targetIds = Arrays.copyOf(targetIds, edgeCount);
    copy.outgoings = Arrays.copyOf(outgoings, edgeCount);

    return copy;
  }

  public DFSCode getParent() {
    DFSCode parent = new DFSCode();

    int lastEdgeTime = getEdgeCount() - 1;

    parent.vertexLabels = getToTime(lastEdgeTime) > getFromTime(lastEdgeTime) ?
      ArrayUtils.subarray(vertexLabels, 0,vertexLabels.length - 1) :
      Arrays.copyOf(vertexLabels, vertexLabels.length);

    int newEdgeArraysSize = lastEdgeTime;

    parent.edgeLabels = ArrayUtils.subarray(edgeLabels, 0,newEdgeArraysSize);
    parent.sourceIds = ArrayUtils.subarray(sourceIds, 0, newEdgeArraysSize);
    parent.targetIds = ArrayUtils.subarray(targetIds, 0, newEdgeArraysSize);
    parent.outgoings = ArrayUtils.subarray(outgoings, 0, newEdgeArraysSize);

    return parent;
  }

  public int[] getRightmostPath() {
    if (rightmostPath == null) {
      setRightmostPath();
    }
    return rightmostPath;
  }

  private void setRightmostPath() {
    // 1-edge pattern
    if (this.getEdgeCount() == 1) {
      // loop
      if (this.getFromTime(0) == this.getToTime( 0)) {
        rightmostPath = new int[] {0};
      } else {
        rightmostPath = new int[] {1, 0};
      }
    } else {
      rightmostPath = new int[0];

      for (int edgeTime = this.getEdgeCount() - 1; edgeTime >= 0; edgeTime--) {
        int fromTime = this.getFromTime(edgeTime);
        int toTime = this.getToTime(edgeTime);
        boolean firstStep = rightmostPath.length == 0;

        // forwards
        if (toTime > fromTime) {

          // first step, add both times
          if (firstStep) {
            rightmostPath = ArrayUtils.add(rightmostPath, toTime);
            rightmostPath = ArrayUtils.add(rightmostPath, fromTime);

            // add from time
          } else if (ArrayUtils.indexOf(rightmostPath, toTime) >= 0) {
            rightmostPath = ArrayUtils.add(rightmostPath, fromTime);
          }

          // first step and loop
        } else if (firstStep && fromTime == toTime) {
          rightmostPath = ArrayUtils.add(rightmostPath, 0);
        }
      }
    }
  }

}
