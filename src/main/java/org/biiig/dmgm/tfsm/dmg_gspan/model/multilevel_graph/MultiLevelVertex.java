package org.biiig.dmgm.tfsm.dmg_gspan.model.multilevel_graph;

import org.apache.commons.lang3.ArrayUtils;

public class MultiLevelVertex implements Comparable<MultiLevelVertex> {
  private int id;
  private final int topLevelLabel;
  private int[] lowerLevelLabels;

  public MultiLevelVertex(int id, int topLevelLabel) {
    this.id = id;
    this.topLevelLabel = topLevelLabel;
  }

  public MultiLevelVertex(int id, Integer topLevelLabel, int[] lowerLevelLabels) {
    this.id = id;
    this.topLevelLabel = topLevelLabel;
    this.lowerLevelLabels = lowerLevelLabels;
  }

  public int getId() {
    return id;
  }

  public int getTopLevelLabel() {
    return topLevelLabel;
  }

  @Override
  public String toString() {
    return "(" + id + ":" + topLevelLabel + ")";
  }


  @Override
  public int compareTo(MultiLevelVertex that) {
    return this.topLevelLabel - that.topLevelLabel;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int[] getLowerLevelLabels() {
    return lowerLevelLabels;
  }

  public int[] getAllLabels() {
    return ArrayUtils.add(lowerLevelLabels, topLevelLabel);
  }
}
