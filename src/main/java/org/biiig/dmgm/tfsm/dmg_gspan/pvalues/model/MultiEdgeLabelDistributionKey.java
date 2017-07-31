package org.biiig.dmgm.tfsm.dmg_gspan.pvalues.model;

/**
 * (smallerLabel, greaterLabel, edgeLabel)
 */
public class MultiEdgeLabelDistributionKey implements Comparable<MultiEdgeLabelDistributionKey> {

  private final String smallerLabel;
  private final String greaterLabel;
  private final String multiEdgeLabel;

  public MultiEdgeLabelDistributionKey(String smallerLabel, String greaterLabel,
    String multiEdgeLabel) {
    this.smallerLabel = smallerLabel;
    this.greaterLabel = greaterLabel;
    this.multiEdgeLabel = multiEdgeLabel;
  }

  public static MultiEdgeLabelDistributionKey create(String sourceLabel, String targetLabel,
    String multiEdgeLabel) {

    return sourceLabel.compareTo(targetLabel) <= 0 ?
      new MultiEdgeLabelDistributionKey(sourceLabel, targetLabel, multiEdgeLabel) :
      new MultiEdgeLabelDistributionKey(targetLabel, sourceLabel, multiEdgeLabel);
  }

  public String getSmallerLabel()
  {
    return smallerLabel;
  }

  public String getGreaterLabel()
  {
    return greaterLabel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MultiEdgeLabelDistributionKey that = (MultiEdgeLabelDistributionKey) o;

    if (smallerLabel != null ? !smallerLabel.equals(that.smallerLabel) :
      that.smallerLabel != null) {
      return false;
    }
    if (greaterLabel != null ? !greaterLabel.equals(that.greaterLabel) :
      that.greaterLabel != null) {
      return false;
    }
    return multiEdgeLabel != null ? multiEdgeLabel.equals(that.multiEdgeLabel) :
      that.multiEdgeLabel == null;
  }

  @Override
  public int hashCode() {
    int result = smallerLabel != null ? smallerLabel.hashCode() : 0;
    result = 31 * result + (greaterLabel != null ? greaterLabel.hashCode() : 0);
    result = 31 * result + (multiEdgeLabel != null ? multiEdgeLabel.hashCode() : 0);
    return result;
  }

  @Override
  public int compareTo(MultiEdgeLabelDistributionKey that) {

    int comparison=this.smallerLabel.compareTo(that.smallerLabel);
    if(comparison==0)
    {
      comparison=this.greaterLabel.compareTo(that.greaterLabel);
      if(comparison==0) {
        return this.multiEdgeLabel.compareTo(that.multiEdgeLabel);
      }
      else
        return comparison;
    }
    else
      return comparison;
  }
}