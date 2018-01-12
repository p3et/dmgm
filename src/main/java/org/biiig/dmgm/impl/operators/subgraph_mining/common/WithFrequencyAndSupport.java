package org.biiig.dmgm.impl.operators.subgraph_mining.common;

public class WithFrequencyAndSupport<T> {

  private final T key;
  private final int support;
  private final int frequency;

  public WithFrequencyAndSupport(T key, int support, int frequency) {
    this.key = key;
    this.support = support;
    this.frequency = frequency;
  }

  public T getKey() {
    return key;
  }

  public int getSupport() {
    return support;
  }

  public int getFrequency() {
    return frequency;
  }
}
