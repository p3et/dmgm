package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.impl.operators.fsm.generalized.MultiDimensionalVector;

import java.util.function.Function;
import java.util.stream.Stream;

public class AllSpecializations implements Function<MultiDimensionalVector, Stream<MultiDimensionalVector>> {

  private final int dimCount;

  AllSpecializations(int dimCount) {
    this.dimCount = dimCount;
  }

  @Override
  public Stream<MultiDimensionalVector> apply(MultiDimensionalVector parent) {
    MultiDimensionalVector[] children = new MultiDimensionalVector[0];

    for (int dim = parent.getLastSpecialization(); dim < dimCount; dim++) {
      MultiDimensionalVector child = parent.getSpecialization(dim);
      if (child != null)
        children = ArrayUtils.add(children, child);
    }

    return Stream.of(children);
  }
}
