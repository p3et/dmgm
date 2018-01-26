package org.biiig.dmgm.impl.operators;

import org.biiig.dmgm.api.HyperVertexOperator;

import java.util.stream.Stream;

public abstract class HyperVertexOperatorBase implements HyperVertexOperator {
  protected boolean parallel = false;

  @Override
  public HyperVertexOperator parallel() {
    this.parallel = true;
    return this;
  }

  @Override
  public HyperVertexOperator sequential() {
    this.parallel = false;
    return this;
  }

  protected <T> Stream<T> setParallelism(Stream<T> stream) {
    return parallel ? stream.parallel() : stream.sequential();
  }
}
