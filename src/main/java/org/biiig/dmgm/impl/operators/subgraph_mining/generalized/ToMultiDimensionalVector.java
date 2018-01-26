package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import org.biiig.dmgm.api.PropertyStore;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningPropertyKeys;

import java.util.Optional;
import java.util.function.Function;

public class ToMultiDimensionalVector implements Function<DFSEmbedding, MultiDimensionalVector> {

  private static final int[] EMPTY_TAXONOMY_PATH = new int[0];
  private final PropertyStore dataStore;

  ToMultiDimensionalVector(PropertyStore dataStore) {
    this.dataStore = dataStore;
  }

  @Override
  public MultiDimensionalVector apply(DFSEmbedding embedding) {
    int vertexCount = embedding.getVertexCount();
    int[][] dimensionPaths = new int[vertexCount][];
    int graphId = embedding.getGraphId();

    for (int vertexTime = 0; vertexTime < vertexCount; vertexTime++) {
      int vertexId = embedding.getVertexId(vertexTime);

      Optional<int[]> optionalTaxonomyPath = dataStore.getVertexIntegers(graphId, vertexId, SubgraphMiningPropertyKeys.TAXONOMY_PATH);
      dimensionPaths[vertexTime] = optionalTaxonomyPath.orElse(EMPTY_TAXONOMY_PATH);
    }

    return MultiDimensionalVector
      .create(embedding, dimensionPaths);
  }
}
