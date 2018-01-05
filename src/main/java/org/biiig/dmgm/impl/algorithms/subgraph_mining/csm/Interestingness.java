package org.biiig.dmgm.impl.algorithms.subgraph_mining.csm;

import java.util.function.BiFunction;

public interface Interestingness extends BiFunction<Float, Float, Boolean> {
  boolean isInteresting(Float categoryCount, Float totalCount);

  @Override
  default Boolean apply(Float categoryCount, Float totalCount) {
    return isInteresting(categoryCount, totalCount);
  }
}
