package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.junit.Test;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class AllSpecializationsTest {

  @Test
  public void apply() {
    Function<MultiDimensionalVector, Stream<MultiDimensionalVector>> function = new AllSpecializations();

    List<MultiDimensionalVector> output = function
      .apply(getInputVector())
      .collect(Collectors.toList());

    assertEquals("output size", 1, output.size());
    assertTrue(getOutputA() + "missing", output.contains(getOutputA()));

  }

  private MultiDimensionalVector getInputVector() {
    int[][] dimensionPaths = new int[3][];
    dimensionPaths[0] = new int[0];
    dimensionPaths[1] = new int[] {1};
    dimensionPaths[2] = new int[] {1, 2};

    return MultiDimensionalVector.create(dimensionPaths);
  }

  private MultiDimensionalVector getOutputA() {
    int[][] dimensionPaths = new int[3][];
    dimensionPaths[0] = new int[0];
    dimensionPaths[1] = new int[0];
    dimensionPaths[2] = new int[] {1, 2};

    return MultiDimensionalVector.create(dimensionPaths);
  }

  private MultiDimensionalVector getOutputAA() {
    int[][] dimensionPaths = new int[3][];
    dimensionPaths[0] = new int[0];
    dimensionPaths[1] = new int[0];
    dimensionPaths[2] = new int[] {1};

    return MultiDimensionalVector.create(dimensionPaths);
  }

  private MultiDimensionalVector getOutputBA() {
    int[][] dimensionPaths = new int[3][];
    dimensionPaths[0] = new int[0];
    dimensionPaths[1] = new int[0];
    dimensionPaths[2] = new int[] {1};

    return MultiDimensionalVector.create(dimensionPaths);
  }

  private MultiDimensionalVector getOutputBB() {
    int[][] dimensionPaths = new int[3][];
    dimensionPaths[0] = new int[0];
    dimensionPaths[1] = new int[] {1};
    dimensionPaths[2] = new int[0];

    return MultiDimensionalVector.create(dimensionPaths);
  }
}