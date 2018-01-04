package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.biiig.dmgm.DMGMTestBase;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiDimensionalVectorTest extends DMGMTestBase{

  @Test
  public void create() {
    assertNotNull("vector must not be null", getVectorA());
  }

  @Test
  public void getSpecialization() {
    assertNull("specialize on empty path dimension", getVectorA().getSpecialization(0));
    assertNotNull("specialize on single-step path dimension", getVectorA().getSpecialization(1));
    assertNotNull("specialize on n-step path dimension", getVectorA().getSpecialization(2));
    assertNull("specialize on fully specialized dimension", getVectorA().getSpecialization(1).getSpecialization(1));
  }

  @Test
  public void testEquals() {
    assertEquals("same vectors", getVectorA(), getVectorA());
    assertNotEquals("not same vectors", getVectorA(), getVectorB());
    assertEquals("only at current state same vectors", getVectorB(), getVectorC());
    assertEquals("different vectors, but same state", getVectorD(), getVectorA().getSpecialization(2));
  }

  @Test
  public void testHashCode() {
    assertEquals("same vectors", getVectorA().hashCode(), getVectorA().hashCode());
    assertNotEquals("not same vectors", getVectorA().hashCode(), getVectorB().hashCode());
    assertEquals("only at current state same vectors", getVectorB().hashCode(), getVectorC().hashCode());
    assertEquals("different vectors, but same state", getVectorD().hashCode(), getVectorA().getSpecialization(2).hashCode());
  }

  private MultiDimensionalVector getVectorA() {
    int[][] dimensionPaths = new int[3][];
    dimensionPaths[0] = new int[0];
    dimensionPaths[1] = new int[] {1};
    dimensionPaths[2] = new int[] {1, 2};

    return MultiDimensionalVector.create(dimensionPaths);
  }

  private MultiDimensionalVector getVectorB() {
    int[][] dimensionPaths = new int[3][];
    dimensionPaths[2] = new int[0];
    dimensionPaths[1] = new int[] {1};
    dimensionPaths[0] = new int[] {1, 2};

    return MultiDimensionalVector.create(dimensionPaths);
  }

  /**
   * should be equal to B in most general state
   */
  private MultiDimensionalVector getVectorC() {
    int[][] dimensionPaths = new int[3][];
    dimensionPaths[2] = new int[0];
    dimensionPaths[1] = new int[] {1};
    dimensionPaths[0] = new int[] {3, 2};

    return MultiDimensionalVector.create(dimensionPaths);
  }

  /**
   * should be equal to A after specialization at index 2
   */
  private MultiDimensionalVector getVectorD() {
    int[][] dimensionPaths = new int[3][];
    dimensionPaths[0] = new int[0];
    dimensionPaths[1] = new int[] {1};
    dimensionPaths[2] = new int[] {1};

    return MultiDimensionalVector.create(dimensionPaths);
  }

}