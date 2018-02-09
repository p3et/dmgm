/*
 * This file is part of Directed Multigraph Miner (DMGM).
 *
 * DMGM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DMGM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DMGM. If not, see <http://www.gnu.org/licenses/>.
 */

package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import org.biiig.dmgm.api.GetProperties;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSEmbedding;

import java.util.function.Function;

public class ToMultiDimensionalVector implements Function<DFSEmbedding, MultiDimensionalVector> {

  private static final int[] EMPTY_TAXONOMY_PATH = new int[0];
  private final GetProperties dataStore;
  private final int taxonomyPathKey;

  ToMultiDimensionalVector(GetProperties dataStore, int taxonomyPathKey) {
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
