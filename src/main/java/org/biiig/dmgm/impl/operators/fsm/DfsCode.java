/*
 * This file is part of Directed Multigraph Miner (DMGM).
 *
 * DMGM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DMGM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DMGM. If not, see <http://www.gnu.org/licenses/>.
 */

package org.biiig.dmgm.impl.operators.fsm;

import java.util.Arrays;
import java.util.function.Function;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.db.SymbolDictionary;
import org.biiig.dmgm.impl.model.CachedGraphBase;

/**
 * A canonical representation of graph patterns.
 * @see <a href="http://ieeexplore.ieee.org/document/1184038/">gSpan</a>
 */
class DfsCode extends CachedGraphBase implements Comparable<DfsCode> {
  //TODO: Optimize this class to not inherit from {@code CachedGraphBase}.

  /**
   * Default graph id from DFS code.
   */
  private static final long DEAFAULT_GRAPH_ID = -1L;

  /**
   * Comparator.
   */
  private final DfsCodeComparator comparator = new DfsCodeComparator();

  /**
   * Indicates for the direction of extensions.
   * A value true at index {@code i} indicates
   * that edge with id {@code i} was traversed in direction.
   */
  private final boolean[] outgoings;

  /**
   * Cache for rightmost path. It is only calculated once only if required.
   */
  private int[] rightmostPath;

  /**
   * Constructor.
   *
   * @param label pattern label
   * @param vertexLabels vertex labels
   * @param edgeLabels edge labels
   * @param sourceIds source ids of edges
   * @param targetIds target ids of edges
   * @param outgoings traversal directions
   */
  public DfsCode(
      int label, int[] vertexLabels, int[] edgeLabels,
      int[] sourceIds, int[] targetIds, boolean[] outgoings) {

    super(DEAFAULT_GRAPH_ID, label, vertexLabels, edgeLabels, sourceIds, targetIds);
    this.outgoings = outgoings;
  }

  /**
   * Get rightmost path. Calculated if not cached.
   *
   * @return rightmost path (vertex times from rightmost to first)
   */
  public int[] getRightmostPath() {
    if (rightmostPath == null) {
      setRightmostPath();
    }
    return rightmostPath;
  }

  /**
   * Calculate and cache rightmost path.
   */
  private void setRightmostPath() {
    // 1-edge pattern
    if (this.getEdgeCount() == 1) {
      // loop
      if (this.getFromTime(0) == this.getToTime(0)) {
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

  /**
   * Extend the DFS code by forwards growth,
   * i.e., by an edge that leads to a previously undiscovered vertex.
   *
   * @param fromTime traversal start time
   * @param toTime traversal end time
   * @param edgeLabel edge label
   * @param outgoing true <=> outgoing traversal
   * @param toLabel label of discovered vertex
   * @return child DFS code
   */
  public DfsCode growForwards(
      int fromTime, int toTime, int edgeLabel, boolean outgoing, int toLabel) {

    return new DfsCode(
      this.label,
      ArrayUtils.add(vertexLabels, toLabel),
      ArrayUtils.add(edgeLabels, edgeLabel),
      ArrayUtils.add(sourceIds, outgoing ? fromTime : toTime),
      ArrayUtils.add(targetIds, outgoing ? toTime : fromTime),
      ArrayUtils.add(outgoings, outgoing)
    );
  }

  /**
   * Extend the DFS code by backwards growth,
   * i.e., by an edge that leads to a vertex that has been visited before (cycle closing).
   *
   * @param fromTime traversal start time
   * @param toTime traversal end time
   * @param edgeLabel edge label
   * @param outgoing true <=> outgoing traversal
   * @return child DFS code
   */
  public DfsCode growBackwards(int fromTime, int toTime, int edgeLabel, boolean outgoing) {
    return new DfsCode(
      this.label,
      Arrays.copyOf(vertexLabels, vertexLabels.length),
      ArrayUtils.add(edgeLabels, edgeLabel),
      ArrayUtils.add(sourceIds, outgoing ? fromTime : toTime),
      ArrayUtils.add(targetIds, outgoing ? toTime : fromTime),
      ArrayUtils.add(outgoings, outgoing)
    );
  }

  /**
   * Test, if a this is parent of a given child DFS code.
   * @param child child code
   * @return true <=> this is parent
   */
  public boolean parentOf(DfsCode child) {
    boolean parent = this.getEdgeCount() <= child.getEdgeCount();

    if (parent) {
      for (int vertexTime = 0; vertexTime < this.getVertexCount(); vertexTime++) {
        parent = this.getVertexLabel(vertexTime) == child.getVertexLabel(vertexTime);

        if (!parent) {
          break;
        }
      }
      if (parent) {
        for (int edgeTime = 0; edgeTime < this.getEdgeCount(); edgeTime++) {
          parent =
              this.getFromTime(edgeTime) == child.getFromTime(edgeTime)
                  && this.getToTime(edgeTime) == child.getToTime(edgeTime)
                  && this.isOutgoing(edgeTime) == child.isOutgoing(edgeTime)
                  && this.getEdgeLabel(edgeTime) == child.getEdgeLabel(edgeTime);
          if (!parent) {
            break;
          }
        }
      }
    }
    return parent;
  }


  /**
   * Get the traversal start vertex.
   * @param edgeTime time of traversal
   * @return time of start (not source) vertex discovery
   */
  public int getFromTime(int edgeTime) {
    return outgoings[edgeTime] ? sourceIds[edgeTime] : targetIds[edgeTime];
  }

  /**
   * Get the traversal end vertex.
   * @param edgeTime time of traversal
   * @return time of end (not target) vertex discovery
   */
  public int getToTime(int edgeTime) {
    return outgoings[edgeTime] ? targetIds[edgeTime] : sourceIds[edgeTime];
  }

  /**
   * Get the direction of a traversal.
   * @param edgeTime time of traversal
   * @return true <=> traversal in direction
   */
  public boolean isOutgoing(int edgeTime) {
    return outgoings[edgeTime];
  }

  @Override
  public String toString() {
    return toString(Object::toString);
  }

  @Override
  public String toString(SymbolDictionary dictionary) {
    return toString(dictionary::decode);
  }

  /**
   * Generalization of toString methods.
   * @param labelFormatter funtion to format labels
   * @return string representation
   */
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DfsCode that = (DfsCode) o;

    return Arrays.equals(this.outgoings, that.outgoings)
        && Arrays.equals(this.vertexLabels, that.vertexLabels)
        && Arrays.equals(this.edgeLabels, that.edgeLabels)
        && Arrays.equals(this.sourceIds, that.sourceIds)
        && Arrays.equals(this.targetIds, that.targetIds);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(vertexLabels)
        + 31 * Arrays.hashCode(edgeLabels)
        + 31 * Arrays.hashCode(sourceIds)
        + 31 * Arrays.hashCode(targetIds)
        + 31 * Arrays.hashCode(outgoings);
  }

  @Override
  public int compareTo(DfsCode that) {
    return comparator.compare(this, that);
  }

  // GETTER

  public int[] getVertexLabels() {
    return vertexLabels;
  }

  public int[] getEdgeLabels() {
    return edgeLabels;
  }

  public int[] getSourceIds() {
    return sourceIds;
  }

  public int[] getTargetIds() {
    return targetIds;
  }

  public boolean[] getOutgoings() {
    return outgoings;
  }

}
