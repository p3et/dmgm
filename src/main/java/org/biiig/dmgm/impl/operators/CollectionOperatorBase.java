package org.biiig.dmgm.impl.operators;

import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.GraphDB;

import java.util.stream.Stream;

public abstract class CollectionOperatorBase implements CollectionOperator {
  protected boolean parallel = false;

  @Override
  public CollectionOperator parallel() {
    this.parallel = true;
    return this;
  }

  @Override
  public CollectionOperator sequential() {
    this.parallel = false;
    return this;
  }


}
