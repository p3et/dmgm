package org.biiig.dmgm.cli;

import org.biiig.dmgm.api.algorithms.tfsm.Operator;

import java.util.Collection;
import java.util.stream.Stream;

public interface GraphCollection {
  static GraphCollection fromCollection(Collection<StringGraph> graphs) {
    return new InMemoryGraphCollection(graphs);
  }

  int size();

  Stream<StringGraph> stream();

  Stream<StringGraph> parallelStream();

  GraphCollection apply(Operator operator);
}
