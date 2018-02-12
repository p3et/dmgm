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

package org.biiig.dmgm.impl.operators.subgraph;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.impl.model.CachedGraphBase;

import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

public class FilterVerticesAndEdgesByLabel implements Function<CachedGraph, CachedGraph> {
  public static final IntPredicate NO_PREDICATE = i -> true;

  private final IntPredicate vertexLabelPredicate;
  private final IntPredicate edgeLabelPredicate;
  private final Boolean dropIsolatedVertices;

  public FilterVerticesAndEdgesByLabel(IntPredicate vertexLabelPredicate, IntPredicate edgeLabelPredicate, Boolean dropIsolatedVertices) {
    this.vertexLabelPredicate = vertexLabelPredicate;
    this.edgeLabelPredicate = edgeLabelPredicate;
    this.dropIsolatedVertices = dropIsolatedVertices;
  }

  @Override
  public CachedGraph apply(CachedGraph graph) {
    int[] vertexCandidates = IntStream
      .range(0, graph.getVertexCount())
      .filter(v -> vertexLabelPredicate.test(graph.getVertexLabel(v)))
      .toArray();

    int[] keepEdges = IntStream
      .range(0, graph.getEdgeCount())
      .filter(v -> edgeLabelPredicate.test(graph.getEdgeLabel(v)))
      .filter(e -> ArrayUtils.contains(vertexCandidates, graph.getSourceId(e)))
      .filter(e -> ArrayUtils.contains(vertexCandidates, graph.getTargetId(e)))
      .toArray();

    int[] keepVertices;

    if (dropIsolatedVertices) {
      IntStream sourceIds = IntStream
        .of(keepEdges)
        .map(graph::getSourceId);

      IntStream targetIds = IntStream
        .of(keepEdges)
        .map(graph::getTargetId);

      keepVertices = IntStream
        .concat(sourceIds, targetIds)
        .distinct()
        .filter(v -> ArrayUtils.contains(vertexCandidates, v))
        .toArray();
    } else {
      keepVertices = vertexCandidates;
    }

    int[] vertexIdMap = new int[graph.getVertexCount()];


    int vertexCount = keepVertices.length;
    int[] vertexLabels = new int[vertexCount];
    for(int outVertexId = 0; outVertexId < vertexCount; outVertexId++)  {
      int inVertexId = keepVertices[outVertexId];
      vertexIdMap[inVertexId] = outVertexId;
      vertexLabels[outVertexId] = graph.getVertexLabel(inVertexId);
    }

    int edgeCount = keepEdges.length;
    int[] edgeLabels = new int[edgeCount];
    int[] sourceIds = new int[edgeCount];
    int[] targetIds = new int[edgeCount];

    int outId = 0;
    for (int inId : keepEdges) {
      edgeLabels[outId] = graph.getEdgeLabel(inId);
      sourceIds[outId] = vertexIdMap[graph.getSourceId(inId)];
      targetIds[outId] = vertexIdMap[graph.getTargetId(inId)];
      outId++;
    }

    return new CachedGraphBase(graph.getId(), graph.getLabel(), vertexLabels, edgeLabels, sourceIds, targetIds);
  }
}
