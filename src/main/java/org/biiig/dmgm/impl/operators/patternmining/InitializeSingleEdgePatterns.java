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

package org.biiig.dmgm.impl.operators.patternmining;

import java.lang.reflect.Array;
import java.util.function.Function;
import java.util.stream.Stream;

import javafx.util.Pair;
import org.biiig.dmgm.api.model.CachedGraph;

/**
 * Extract all 1-edge DFS codes and embeddings from a graph.
 */
public class InitializeSingleEdgePatterns
    implements Function<CachedGraph, Stream<Pair<DfsCode, WithEmbedding>>> {

  /**
   * Pattern label.
   */
  private final int label;

  InitializeSingleEdgePatterns(int label) {
    this.label = label;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Stream<Pair<DfsCode,WithEmbedding>> apply(CachedGraph graph) {

    int edgeCount = graph.getEdgeCount();
    Pair<DfsCode, WithEmbedding>[] pairs =
        (Pair<DfsCode, WithEmbedding>[]) Array.newInstance(Pair.class, edgeCount);

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

      DfsCode dfsCode = new DfsCode(
          label, loop ? new int[] {fromLabel} : new int[] {fromLabel, toLabel},
          new int[] {edgeLabel},
          new int[] {outgoing ? fromTime : toTime},
          new int[] {outgoing ? toTime : fromTime},
          new boolean[] {outgoing}
      );


      DfsEmbedding embedding = new DfsEmbedding(graph.getId(), fromId, edgeId, toId);

      pairs[edgeId] = new Pair<>(dfsCode, embedding);
    }


    return Stream.of(pairs);
  }

}
