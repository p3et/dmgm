package org.biiig.dmgm.todo.pvalues.model;


/**
 * (outgoing, multiplicity, format)
 */
public class VertexDegreeKey implements Comparable<VertexDegreeKey> {

  private final boolean outgoing;
  private final Integer multiplicity;
  private final String label;

  public VertexDegreeKey(boolean outgoing, Integer multiplicity, String label) {
    this.outgoing = outgoing;
    this.multiplicity = multiplicity;
    this.label = label;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    VertexDegreeKey that = (VertexDegreeKey) o;

    if (outgoing != that.outgoing) {
      return false;
    }
    if (multiplicity != null ? !multiplicity.equals(that.multiplicity) :
      that.multiplicity != null) {
      return false;
    }
    return label != null ? label.equals(that.label) : that.label == null;
  }

  @Override
  public int hashCode() {
    int result = (outgoing ? 1 : 0);
    result = 31 * result + (multiplicity != null ? multiplicity.hashCode() : 0);
    result = 31 * result + (label != null ? label.hashCode() : 0);
    return result;
  }

  @Override
  public int compareTo(VertexDegreeKey that) {
    int comparison;

    if (this.outgoing==that.outgoing) {
      comparison = this.multiplicity - that.multiplicity;

      if (comparison == 0) {
        comparison = this.label.compareTo(that.label);
      }

    } else {
      comparison = this.outgoing ? -1 : 1;
    }

    return comparison;
  }
}
