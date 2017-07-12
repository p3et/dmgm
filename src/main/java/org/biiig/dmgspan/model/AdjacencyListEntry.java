package org.biiig.dmgspan.model;

/**
 * Created by peet on 12.07.17.
 */
public class AdjacencyListEntry implements  Comparable<AdjacencyListEntry> {
  private final boolean loop;
  private final boolean outgoing;
  private final int edgeId;
  private final int edgeLabel;
  private final int toId;
  private final int toLabel;

  public AdjacencyListEntry(
    boolean loop, boolean outgoing, int edgeId, int edgeLabel, int toId, int toLabel) {
    this.loop = loop;
    this.outgoing = outgoing;
    this.edgeId = edgeId;
    this.edgeLabel = edgeLabel;
    this.toId = toId;
    this.toLabel = toLabel;
  }

  public boolean isLoop() {
    return loop;
  }

  public boolean isOutgoing() {
    return outgoing;
  }

  public int getEdgeLabel() {
    return edgeLabel;
  }

  public int getToId() {
    return toId;
  }

  public int getToLabel() {
    return toLabel;
  }

  @Override
  public int compareTo(AdjacencyListEntry that) {

    int comparison;

    if (this.loop == that.loop) {
      if (this.outgoing == that.outgoing) {
        comparison = this.edgeLabel - that.edgeLabel;

        if (comparison == 0) {
          comparison = this.toLabel - that.toLabel;
        }
      } else {
        comparison = this.outgoing ? -1 : 1;
      }
    } else {
      comparison = this.loop ? -1 : 1;
    }

    return comparison;
  }

  @Override
  public String toString() {
    return loop + ":" + edgeLabel + ">" + toLabel + ":" + toId;
  }

  public int getEdgeId() {
    return edgeId;
  }
}
