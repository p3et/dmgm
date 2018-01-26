package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import javafx.util.Pair;

import java.util.function.Predicate;

public class LabelFrequencyFilter<T> implements Predicate<Pair<T, Integer>> {
  private final int minSupport;

  public LabelFrequencyFilter(int minSupport) {
    this.minSupport = minSupport;
  }

  @Override
  public boolean test(Pair<T, Integer> pair) {
    return pair.getValue() >= minSupport;
  }
}
