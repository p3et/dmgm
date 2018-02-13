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

package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.model.CachedGraph;

import java.lang.reflect.Array;

public abstract class GrowChildrenBase implements GrowChildren {

  @SuppressWarnings("unchecked")
  @Override
  public Pair<DFSCode,DFSEmbedding>[] apply(
    CachedGraph graph, DFSCode parentCode, int[] rightmostPath, DFSEmbedding parentEmbedding) {

    Pair<DFSCode,DFSEmbedding>[] children = (Pair<DFSCode, DFSEmbedding>[]) Array.newInstance(Pair.class, 0);

    boolean rightmost = true;
    for (int fromTime : parentCode.getRightmostPath()) {
      int fromId = parentEmbedding.getVertexId(fromTime);

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
            DFSCode childCode = parentCode.addEdge(fromTime, toTime, graph.getEdgeLabel(edgeId), isOutgoing(), toLabel);

            DFSEmbedding childEmbedding = parentEmbedding.expandByEdgeId(edgeId);

            children =
              ArrayUtils.add(children, new Pair<>(childCode, childEmbedding));

            // grow backwards from to
          } else if (toTime < 0) {
            int toLabel = graph.getVertexLabel(toId);
            toTime = parentCode.getVertexCount();
            DFSCode childCode = parentCode.addEdge(fromTime, toTime, graph.getEdgeLabel(edgeId), isOutgoing(), toLabel);

            DFSEmbedding childEmbedding = parentEmbedding.expandByEdgeIdAndVertexId(edgeId, toId);

            children =
              ArrayUtils.add(children, new Pair<>(childCode, childEmbedding));            }
        }
      }

      rightmost = false;
    }

    return children;
  }

  protected abstract int[] getEdgeIds(CachedGraph graph, int fromId);

  protected abstract int getToId(CachedGraph graph, int edgeId);

  protected abstract boolean isOutgoing();
}
