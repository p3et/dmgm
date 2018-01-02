package org.biiig.dmgm.impl.algorithms.fsm.ccp;

import java.util.function.BiFunction;

public interface Interestingness extends BiFunction<Integer, Integer, Boolean> {
  boolean isInteresting(int categoryCount, int totalCount);

  @Override
  default Boolean apply(Integer categoryCount, Integer totalCount) {
    return isInteresting(categoryCount, totalCount);
  }
}
