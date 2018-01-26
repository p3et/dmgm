package org.biiig.dmgm.impl.operators.subgraph_mining.frequent;

import javafx.util.Pair;

import java.util.function.Predicate;

public class FrequencyFilter<T> implements Predicate<Pair<T, Long>> {
  private final long minSupport;

  public FrequencyFilter(long minSupport) {
    this.minSupport = minSupport;
  }

  @Override
  public boolean test(Pair<T, Long> pair) {
    return pair.getValue() >= minSupport;
  }
}
