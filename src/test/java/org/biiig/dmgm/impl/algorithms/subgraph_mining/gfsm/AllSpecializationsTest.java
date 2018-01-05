package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class AllSpecializationsTest {

  @Test
  public void apply() {
    Function<MultiDimensionalVector, Stream<MultiDimensionalVector>> function = new AllSpecializations(3);

    List<MultiDimensionalVector> output = Lists.newArrayList(getInputVector());

    int[] expectedResultCounts = new int[] {2, 2, 1, 0};

    for (int count : expectedResultCounts) {
      output = output
        .stream()
        .flatMap(function)
        .collect(Collectors.toList());

      assertEquals("output size", count, output.size());
    }
  }

  private MultiDimensionalVector getInputVector() {
    int[][] dimensionPaths = new int[3][];
    dimensionPaths[0] = new int[0];
    dimensionPaths[1] = new int[] {1};
    dimensionPaths[2] = new int[] {1, 2};

    return MultiDimensionalVector.create(0, dimensionPaths);
  }
}