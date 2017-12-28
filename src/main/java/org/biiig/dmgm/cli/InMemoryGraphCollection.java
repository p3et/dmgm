package org.biiig.dmgm.cli;

import org.biiig.dmgm.api.algorithms.tfsm.Operator;

import java.util.Collection;
import java.util.stream.Stream;

public class InMemoryGraphCollection implements GraphCollection {
  private final Collection<StringGraph> graphs;

  public InMemoryGraphCollection(Collection<StringGraph> graphs) {
    this.graphs = graphs;
  }

  @Override
  public int size() {
    return graphs.size();
  }

  @Override
  public Stream<StringGraph> stream() {
    return graphs.stream();
  }

  @Override
  public Stream<StringGraph> parallelStream() {
    return graphs.parallelStream();
  }

  @Override
  public GraphCollection apply(Operator operator) {
    return operator.apply(this);
  }

}
