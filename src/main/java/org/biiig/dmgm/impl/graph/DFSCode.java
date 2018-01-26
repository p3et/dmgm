package org.biiig.dmgm.impl.graph;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.HyperVertexDB;

import java.util.Arrays;
import java.util.function.Function;

public class DFSCode extends SmallGraphBase implements Comparable<DFSCode> {

  private final boolean[] outgoings;

  private final DFSCodeComparator comparator = new DFSCodeComparator();
  private int[] rightmostPath;

  public DFSCode(int[] vertexLabels, int[] edgeLabels, int[] sourceIds, int[] targetIds, boolean[] outgoings) {
    super(-1l, -1, vertexLabels, edgeLabels, sourceIds, targetIds);
    this.outgoings = outgoings;
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
      if (parent)
        for (int edgeTime = 0; edgeTime < this.getEdgeCount(); edgeTime++) {
          parent =
            this.getFromTime(edgeTime) == child.getFromTime(edgeTime) &&
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

  public DFSCode addEdge(int fromTime, int toTime, int label, boolean outgoing, int toLabel) {
    return new DFSCode(
      toTime > fromTime ?
        ArrayUtils.add(vertexLabels, toLabel) :
        Arrays.copyOf(vertexLabels, vertexLabels.length),
      ArrayUtils.add(edgeLabels, label),
      ArrayUtils.add(sourceIds, outgoing ? fromTime : toTime),
      ArrayUtils.add(targetIds, outgoing ? toTime : fromTime),
      ArrayUtils.add(outgoings, outgoing)
    );
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


  @Override
  public String toString(HyperVertexDB db) {
    return toString(db::decode);
  }

  @Override
  public String toString() {
    return toString(Object::toString);
  }

  private String toString(Function<Integer, String> labelFormatter) {
    StringBuilder builder = new StringBuilder();

    for (int edgeTime = 0; edgeTime < getEdgeCount(); edgeTime++) {
      boolean outgoing = isOutgoing(edgeTime);
      int fromTime = getFromTime(edgeTime);
      int toTime = getToTime(edgeTime);

      String fromLabel = labelFormatter.apply(getVertexLabel(fromTime));
      String edgeLabel = labelFormatter.apply(getEdgeLabel(edgeTime));
      String toLabel = labelFormatter.apply(getVertexLabel(toTime));

      builder
        .append(fromTime)
        .append(":")
        .append(fromLabel)
        .append(outgoing ? "-" : "<-")
        .append(edgeLabel)
        .append(outgoing ? "->" : "-")
        .append(toTime)
        .append(":")
        .append(toLabel)
        .append(" ");
    }

    return builder.toString();
  }

}
