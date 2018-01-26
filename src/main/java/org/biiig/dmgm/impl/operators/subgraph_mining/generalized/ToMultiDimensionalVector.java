package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import org.biiig.dmgm.api.PropertyStore;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSEmbedding;

import java.util.Optional;
import java.util.function.Function;

public class ToMultiDimensionalVector implements Function<DFSEmbedding, MultiDimensionalVector> {

  private static final int[] EMPTY_TAXONOMY_PATH = new int[0];
  private final PropertyStore dataStore;
  private final int taxonomyPathKey;

  ToMultiDimensionalVector(PropertyStore dataStore, int taxonomyPathKey) {
    this.dataStore = dataStore;
    this.taxonomyPathKey = taxonomyPathKey;
  }

  @Override
  public MultiDimensionalVector apply(DFSEmbedding embedding) {
    int vertexCount = embedding.getVertexCount();
    int[][] dimensionPaths = new int[vertexCount][];
    long graphId = embedding.getGraphId();

    for (int vertexTime = 0; vertexTime < vertexCount; vertexTime++) {
      int vertexId = embedding.getVertexId(vertexTime);

      int[] path = dataStore.getInts(vertexId, taxonomyPathKey);
      dimensionPaths[vertexTime] = path != null ? path : EMPTY_TAXONOMY_PATH;
    }

    return MultiDimensionalVector
      .create(embedding, dimensionPaths);
  }
}
