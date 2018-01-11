package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSEmbedding;
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
    assertEquals("last specialization", 2, getVectorA().getSpecialization(2).getLastSpecialization());
  }

  @Test
  public void testEquals() {
    assertEquals("same vectors", getVectorA(), getVectorA());
    assertEquals("both fully general", getVectorA(), getVectorB());
    assertNotEquals("after generalization",
      getVectorA().getSpecialization(1), getVectorB().getSpecialization(1));
  }

  @Test
  public void testHashCode() {
    assertEquals("same vectors", getVectorA().hashCode(), getVectorA().hashCode());
    assertEquals("both fully general", getVectorA().hashCode(), getVectorB().hashCode());
    assertNotEquals("after generalization",
      getVectorA().getSpecialization(1).hashCode(), getVectorB().getSpecialization(1).hashCode());
  }

  private MultiDimensionalVector getVectorA() {
    int[][] dimensionPaths = new int[3][];
    dimensionPaths[0] = new int[0];
    dimensionPaths[1] = new int[] {1};
    dimensionPaths[2] = new int[] {1, 2};

    return MultiDimensionalVector.create(null, dimensionPaths);
  }

  private MultiDimensionalVector getVectorB() {
    int[][] dimensionPaths = new int[3][];
    dimensionPaths[0] = new int[0];
    dimensionPaths[1] = new int[] {2};
    dimensionPaths[2] = new int[] {1, 2};

    return MultiDimensionalVector.create(null, dimensionPaths);
  }
}