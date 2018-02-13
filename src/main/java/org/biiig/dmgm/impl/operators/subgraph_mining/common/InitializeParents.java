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
import org.biiig.dmgm.api.model.CachedGraph;

import java.lang.reflect.Array;
import java.util.function.Function;
import java.util.stream.Stream;

public class InitializeParents<G extends CachedGraph> implements Function<G, Stream<Pair<DFSCode,DFSEmbedding>>> {

  private final int label;

  public InitializeParents(int label) {
    this.label = label;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Stream<Pair<DFSCode,DFSEmbedding>> apply(G graph) {

    int edgeCount = graph.getEdgeCount();
    Pair<DFSCode,DFSEmbedding>[] pairs = (Pair<DFSCode, DFSEmbedding>[]) Array.newInstance(Pair.class, edgeCount);

    for (int edgeId = 0; edgeId < edgeCount; edgeId++) {

      int sourceId = graph.getSourceId(edgeId);
      int targetId = graph.getTargetId(edgeId);
      boolean loop = sourceId == targetId;

      int fromTime = 0;
      int toTime = loop ? 0 : 1;

      int fromLabel;
      boolean outgoing;
      int edgeLabel = graph.getEdgeLabel(edgeId);
      int toLabel;

      int fromId;
      int toId;

      int sourceLabel = graph.getVertexLabel(sourceId);
      int targetLabel = graph.getVertexLabel(targetId);

      if (sourceLabel <= targetLabel) {
        fromId = sourceId;
        fromLabel = sourceLabel;

        outgoing = true;

        toId = targetId;
        toLabel = targetLabel;
      } else {
        fromId = targetId;
        fromLabel = targetLabel;

        outgoing = false;

        toId = sourceId;
        toLabel = sourceLabel;
      }

      DFSCode dfsCode = new DFSCode(
        label, loop ? new int[] {fromLabel} : new int[] {fromLabel, toLabel},
        new int[] {edgeLabel},
        new int[] {outgoing ? fromTime : toTime},
        new int[] {outgoing ? toTime : fromTime},
        new boolean[] {outgoing}
      );


      DFSEmbedding embedding = new DFSEmbedding(graph.getId(), fromId, edgeId, toId);
      Pair<DFSCode,DFSEmbedding> codeEmbeddingPair = new Pair<>(dfsCode, embedding);

      pairs[edgeId] = codeEmbeddingPair;
    }


    return Stream.of(pairs);
  }

}
