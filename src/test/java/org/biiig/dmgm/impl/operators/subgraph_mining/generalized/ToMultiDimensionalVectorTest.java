package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.PropertyStore;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningPropertyKeys;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class ToMultiDimensionalVectorTest extends DMGMTestBase {

  @Test
  public void apply() {
    PropertyStore dataStore = new InMemoryPropertyStore();

    dataStore.setVertex(0, 2, SubgraphMiningPropertyKeys.TAXONOMY_PATH, new int[] {1, 2});

    DFSEmbedding embeddingA = new DFSEmbedding(0, new int[]{1, 2}, new int[0]);
    DFSEmbedding embeddingB = new DFSEmbedding(0, new int[]{1, 3}, new int[0]);


    Function<DFSEmbedding, MultiDimensionalVector> function = new ToMultiDimensionalVector(dataStore);

    assertEquals("with data", getVectorA(), function.apply(embeddingA));
    assertEquals("without data", getVectorB(), function.apply(embeddingB));
  }

  private MultiDimensionalVector getVectorA() {
    int[][] dimensionPaths = new int[2][];
    dimensionPaths[0] = new int[0];
    dimensionPaths[1] = new int[] {1, 2};

    return MultiDimensionalVector.create(null, dimensionPaths);
  }

  private MultiDimensionalVector getVectorB() {
    int[][] dimensionPaths = new int[2][];
    dimensionPaths[0] = new int[0];
    dimensionPaths[1] = new int[0];

    return MultiDimensionalVector.create(null, dimensionPaths);
  }
}