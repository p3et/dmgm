package org.biiig.dmgm.impl.operators;

import org.biiig.dmgm.api.Operator;

import java.util.stream.Stream;

public abstract class OperatorBase implements Operator {
  protected boolean parallel = false;

  @Override
  public Operator parallel() {
    this.parallel = true;
    return this;
  }

  @Override
  public Operator sequential() {
    this.parallel = false;
    return this;
  }

  protected <T> Stream<T> setParallelism(Stream<T> stream) {
    return parallel ? stream.parallel() : stream.sequential();
  }
}
