package org.biiig.dmgm.api;

import java.util.stream.Stream;


public interface GraphCollection {
  int size();

  Stream<SmallGraph> stream();
  Stream<SmallGraph> parallelStream();
  EPGMDatabase getDatabase();

  default GraphCollection apply(Operator operator) {
    return operator.apply(this);
  }
}
