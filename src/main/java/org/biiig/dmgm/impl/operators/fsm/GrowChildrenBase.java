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

import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.model.CachedGraph;

import java.util.Collection;

public abstract class GrowChildrenBase implements GrowChildren {

  private final DFSCode parent;

  GrowChildrenBase(DFSCode parent) {
    this.parent = parent;
  }

  @Override
  public void addChildren(CachedGraph withGraph, DFSEmbedding parentEmbedding, Collection<Pair<DFSCode, WithDFSEmbedding>> output) {
    boolean rightmost = true;
    for (int fromTime : parent.getRightmostPath()) {
      if (fromTime >= parentEmbedding.getVertexCount()) {
        System.out.println(parent);
        System.out.println(parent.getVertexCount());
        System.out.println(parentEmbedding);
        System.out.println(ArrayUtils.toString(parent.getRightmostPath()));
      }

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
            DFSCode childCode = parent.addBackwardsEdge(fromTime, toTime, graph.getEdgeLabel(edgeId), isOutgoing());

            DFSEmbedding childEmbedding = parentEmbedding.expandByEdgeId(edgeId);

            output.add(new Pair<>(childCode, childEmbedding));

            // grow forwards
          } else if (toTime < 0) {
            int toLabel = graph.getVertexLabel(toId);
            toTime = parent.getVertexCount();
            DFSCode childCode = parent.addForwardsEdge(fromTime, toTime, graph.getEdgeLabel(edgeId), isOutgoing(), toLabel);

            DFSEmbedding childEmbedding = parentEmbedding.expandByEdgeIdAndVertexId(edgeId, toId);

            output.add(new Pair<>(childCode, childEmbedding));

          }
        }
      }

      rightmost = false;
    }
  }

  protected abstract int[] getEdgeIds(CachedGraph graph, int fromId);

  protected abstract int getToId(CachedGraph graph, int edgeId);

  protected abstract boolean isOutgoing();
}
