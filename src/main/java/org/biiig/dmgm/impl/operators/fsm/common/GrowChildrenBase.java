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

package org.biiig.dmgm.impl.operators.fsm.common;

import javafx.util.Pair;
import org.biiig.dmgm.api.model.CachedGraph;

import java.util.Collection;
import java.util.function.BiFunction;

public abstract class GrowChildrenBase<G extends WithGraph, E extends WithEmbedding> implements GrowChildren<G, E> {

  private final DFSCode parent;
  private final BiFunction<G, DFSEmbedding, E> embeddingFactory;

  GrowChildrenBase(DFSCode parent, BiFunction<G, DFSEmbedding, E> embeddingFactory) {
    this.parent = parent;
    this.embeddingFactory = embeddingFactory;
  }

  @Override
  public void addChildren(G withGraph, DFSEmbedding parentEmbedding, Collection<Pair<DFSCode, E>> output) {
    boolean rightmost = true;
    for (int fromTime : parent.getRightmostPath()) {
      int fromId = parentEmbedding.getVertexId(fromTime);

      CachedGraph graph = withGraph.getGraph();
      for (int edgeId : getEdgeIds(graph, fromId)) {
        // if not contained in parent embedding
        if (! parentEmbedding.containsEdgeId(edgeId)) {

          // determine times of incident vertices in parent embedding
          int toId = getToId(graph, edgeId);
          int toTime = parentEmbedding.getVertexTime(toId);

          // CHECK FOR BACKWARDS GROWTH OPTIONS

          // grow backwards
          if (rightmost && toTime >= 0) {
            int toLabel = graph.getVertexLabel(toId);
            DFSCode childCode = parent.addEdge(fromTime, toTime, graph.getEdgeLabel(edgeId), isOutgoing(), toLabel);

            DFSEmbedding childEmbedding = parentEmbedding.expandByEdgeId(edgeId);

            add(output, withGraph, childCode, childEmbedding);

            // grow backwards from to
          } else if (toTime < 0) {
            int toLabel = graph.getVertexLabel(toId);
            toTime = parent.getVertexCount();
            DFSCode childCode = parent.addEdge(fromTime, toTime, graph.getEdgeLabel(edgeId), isOutgoing(), toLabel);

            DFSEmbedding childEmbedding = parentEmbedding.expandByEdgeIdAndVertexId(edgeId, toId);

            add(output, withGraph, childCode, childEmbedding);
          }
        }
      }

      rightmost = false;
    }
  }

  public void add(Collection<Pair<DFSCode, E>> output, G withGraph, DFSCode childCode, DFSEmbedding childEmbedding) {
    output.add(new Pair<>(childCode, embeddingFactory.apply(withGraph, childEmbedding)));
  }

  protected abstract int[] getEdgeIds(CachedGraph graph, int fromId);

  protected abstract int getToId(CachedGraph graph, int edgeId);

  protected abstract boolean isOutgoing();
}
