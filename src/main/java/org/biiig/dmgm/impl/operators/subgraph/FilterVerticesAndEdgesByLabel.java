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

import java.util.function.IntPredicate;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

/**
 * Take a graph an filter vertices and edges by given label predicates.
 */
public class FilterVerticesAndEdgesByLabel implements UnaryOperator<CachedGraph> {

  /**
   * Vertex label predicate.
   */
  private final IntPredicate vertexLabelPredicate;
  /**
   * Edge label predicate.
   */
  private final IntPredicate edgeLabelPredicate;
  /**
   * Flag to drop isolated vertices,
   * i.e., those who are neither source not target of any edge.
   */
  private final Boolean dropIsolatedVertices;

  /**
   * Constructor.
   *
   * @param vertexLabelPredicate vertex predicate
   * @param edgeLabelPredicate edge predicate
   * @param dropIsolatedVertices flag to trigger the deletion of isolated vertices
   */
  public FilterVerticesAndEdgesByLabel(
      IntPredicate vertexLabelPredicate, IntPredicate edgeLabelPredicate,
      Boolean dropIsolatedVertices) {

    this.vertexLabelPredicate = vertexLabelPredicate;
    this.edgeLabelPredicate = edgeLabelPredicate;
    this.dropIsolatedVertices = dropIsolatedVertices;
  }

  @Override
  public CachedGraph apply(CachedGraph graph) {
    // apply vertex predicate
    int inVertexCount = graph.getVertexCount();
    int[] candidateVertexIds = IntStream
      .range(0, inVertexCount)
      .filter(v -> vertexLabelPredicate.test(graph.getVertexLabel(v)))
      .toArray();

    // apply edge predicate and filter edges without source or target
    int[] outEdgeIds = IntStream
      .range(0, graph.getEdgeCount())
      .filter(v -> edgeLabelPredicate.test(graph.getEdgeLabel(v)))
      .filter(e -> ArrayUtils.contains(candidateVertexIds, graph.getSourceId(e)))
      .filter(e -> ArrayUtils.contains(candidateVertexIds, graph.getTargetId(e)))
      .toArray();

    // vertices to keep
    int[] outVertexIds;

    if (dropIsolatedVertices) {
      // drop vertices without edges
      IntStream sourceIds = IntStream
          .of(outEdgeIds)
          .map(graph::getSourceId);

      IntStream targetIds = IntStream
          .of(outEdgeIds)
          .map(graph::getTargetId);

      outVertexIds = IntStream
          .concat(sourceIds, targetIds)
          .distinct()
          .filter(v -> ArrayUtils.contains(candidateVertexIds, v))
          .toArray();
    } else {
      // keep all vertices
      outVertexIds = candidateVertexIds;
    }

    // in vertex id -> out vertex id
    int[] vertexIdMap = new int[inVertexCount];

    // create out vertex data
    int outVertexCount = outVertexIds.length;
    int[] outVertexLabels = new int[outVertexCount];
    for (int outVertexId = 0; outVertexId < outVertexCount; outVertexId++)  {
      int inVertexId = outVertexIds[outVertexId];
      vertexIdMap[inVertexId] = outVertexId;
      outVertexLabels[outVertexId] = graph.getVertexLabel(inVertexId);
    }

    // create out edge data
    int edgeCount = outEdgeIds.length;
    int[] outEdgeLabels = new int[edgeCount];
    int[] outSourceIds = new int[edgeCount];
    int[] outTargetIds = new int[edgeCount];

    int outId = 0;
    for (int inId : outEdgeIds) {
      outEdgeLabels[outId] = graph.getEdgeLabel(inId);
      outSourceIds[outId] = vertexIdMap[graph.getSourceId(inId)];
      outTargetIds[outId] = vertexIdMap[graph.getTargetId(inId)];
      outId++;
    }

    return new CachedGraphBase(
      graph.getId(), graph.getLabel(), outVertexLabels, outEdgeLabels, outSourceIds, outTargetIds);
  }
}
