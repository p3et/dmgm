package org.biiig.dmgm.tfsm.dmg_gspan.impl.gspan;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Objects;

public class DFSCode implements Comparable<DFSCode> {
  private static final int TRUE_VALUE = 0;
  private static final int FALSE_VALUE = 1;
  private static final int EDGE_LENGTH = 6;


  private static final int FROM_TIME = 0;
  private static final int TO_TIME = 1;
  private static final int FROM_LABEL = 2;
  private static final int OUTGOING = 3;
  private static final int EDGE_LABEL = 4;
  private static final int TO_LABEL = 5;
  private int[][] steps;
  private final LexicographicDFSCodeComparator comparator = new LexicographicDFSCodeComparator();

  public DFSCode(int fromTime, int toTime, int fromLabel, boolean outgoing, int edgeLabel,
    int toLabel) {

    int[] step = getStep(fromTime, toTime, fromLabel, outgoing, edgeLabel, toLabel);

    this.steps = new int[][] {step};

  }

  private int[] getStep(int fromTime, int toTime, int fromLabel, boolean outgoing, int edgeLabel,
    int toLabel) {
    int[] step = new int[EDGE_LENGTH];

    step[FROM_TIME] = fromTime;
    step[TO_TIME] = toTime;
    step[FROM_LABEL] = fromLabel;
    step[OUTGOING] = outgoing ? TRUE_VALUE : FALSE_VALUE;
    step[EDGE_LABEL] = edgeLabel;
    step[TO_LABEL] = toLabel;
    return step;
  }

  private DFSCode(int[][] steps) {
    this.steps = steps;
  }

  public int size() {
    return this.steps.length;
  }

  public int[][] getSteps() {
    return steps;
  }

  public int getFromTime(int edgeTime) {
    return steps[edgeTime][FROM_TIME];
  }

  public int getToTime(int edgeTime) {
    return steps[edgeTime][TO_TIME];
  }

  public int getFromLabel(int edgeTime) {
    return steps[edgeTime][FROM_LABEL];
  }

  public boolean isOutgoing(int edgeTime) {
    return steps[edgeTime][OUTGOING] == TRUE_VALUE;
  }

  public int getEdgeLabel(int edgeTime) {
    return steps[edgeTime][EDGE_LABEL];
  }

  public int getToLabel(int edgeTime) {
    return steps[edgeTime][TO_LABEL];
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    for (int[] step : steps) {
      boolean outgoing = step[OUTGOING] == TRUE_VALUE;
      builder
        .append(step[FROM_TIME])
        .append(":")
        .append(step[FROM_LABEL])
        .append(outgoing ? "-" : "<-")
        .append(step[EDGE_LABEL])
        .append(outgoing ? "->" : "-")
        .append(step[TO_TIME])
        .append(":")
        .append(step[TO_LABEL])
        .append(" ");
    }


    return builder.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DFSCode dfsCode = (DFSCode) o;

    return Arrays.deepEquals(steps, dfsCode.steps);
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(steps);
  }

  @Override
  public int compareTo(DFSCode that) {
    return comparator.compare(this, that);
  }

  public DFSCode deepCopy() {
    int[][] steps = new int[this.steps.length][];

    for (int i = 0; i<steps.length; i++){
      steps[i] = this.steps[i].clone();
    }

    return new DFSCode(steps);
  }

  public void grow(
    int fromTime, int toTime, int fromLabel, boolean outgoing, int edgeLabel,
    int toLabel) {

    int[] step = getStep(fromTime, toTime, fromLabel, outgoing, edgeLabel, toLabel);

    this.steps = ArrayUtils.add(this.steps, step);

  }

  public boolean parentOf(DFSCode child) {
    boolean parent = this.size() <= child.size();

    if (parent) {
      for (int i = 0; i < this.size(); i++) {
        parent = Objects.deepEquals(this.steps[i], child.steps[i]);
        if (!parent) {
          break;
        }
      }
    }

    return parent;
  }

  public int getVertexCount() {
    int vertexCount = 0;

    for (int[] step : steps) {
      vertexCount = Math.max(vertexCount, step[TO_TIME]);
    }

    return vertexCount + 1;
  }

  public void setVertexLabel(int vertexTime, int label) {
    for (int i =0; i < steps.length; i++) {
      int[] step = steps[i];

      if (step[FROM_TIME] == vertexTime) {
        step = step.clone();
        step[FROM_LABEL] = label;
        steps[i] = step;
      }
      if (step[TO_TIME] == vertexTime) {
        step = step.clone();
        step[TO_LABEL] = label;
        steps[i] = step;
      }
    }
  }
}
