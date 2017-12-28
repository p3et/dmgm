package org.biiig.dmgm.cli;

import java.util.Collection;

public class StringGraphCollection {
  private final Collection<StringGraph> graphs;

  public StringGraphCollection(Collection<StringGraph> graphs) {
    this.graphs = graphs;
  }

  public static StringGraphCollection fromCollection(Collection<StringGraph> graphs) {
    return new StringGraphCollection(graphs);
  }

  public int size() {
    return graphs.size();
  }
}
