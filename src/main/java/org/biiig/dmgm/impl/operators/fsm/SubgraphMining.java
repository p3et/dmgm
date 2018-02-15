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

package org.biiig.dmgm.impl.operators.fsm;

import org.biiig.dmgm.api.operators.CollectionToCollectionOperator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * A subgraph mining algorithm.
 *
 * @param <G> graph type (e.g., with category or with taxonomy paths)
 * @param <S> support type (e.g., simple support or map: category -> support
 */
public interface SubgraphMining<G extends WithGraph, S>
  extends CollectionToCollectionOperator, SubgraphMiningSupportMethods<G, S>, SubgraphMiningVariantMethods<G, S> {
  /**
   * 1. Sort embeddings by graph id.
   * 2. Iterate over embeddings
   * 3. Load graph for any new graph id
   * 4. Execute biconsumer for any embedding-graph pair
   *
   * @param embeddings embedding ...
   * @param graphIndex map: graph id -> graph
   * @param joinFunction action executed for every pair
   */
  default void mergeJoinAndExecute(List<WithDFSEmbedding> embeddings, Map<Long, G> graphIndex, BiConsumer<G, WithDFSEmbedding> joinFunction) {
    embeddings.sort(Comparator.comparing(e -> e.getEmbedding().getGraphId()));
    Iterator<WithDFSEmbedding> iterator = embeddings.iterator();
    G withGraph = null;

    // for each embedding
    while (iterator.hasNext()) {
      WithDFSEmbedding withEmbedding = iterator.next();
      DFSEmbedding embedding = withEmbedding.getEmbedding();
      long graphId = embedding.getGraphId();

      // load new graph if required
      if (withGraph == null || graphId != withGraph.getGraph().getId())
        withGraph = graphIndex.get(graphId);

      // grow all children and add to list
      joinFunction.accept(withGraph, withEmbedding);
    }
  }
}
