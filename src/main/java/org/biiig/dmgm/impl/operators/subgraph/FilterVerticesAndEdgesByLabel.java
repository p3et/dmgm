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
import org.biiig.dmgm.api.model.GraphView;
import org.biiig.dmgm.impl.model.GraphViewBase;
import org.biiig.dmgm.impl.util.arrays.DmgmArrayUtils;
import org.biiig.dmgm.impl.util.arrays.IntArrayBuilder;

import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

/**
 * Take a graph an filter vertices and edges by given label predicates.
 */
public class FilterVerticesAndEdgesByLabel implements UnaryOperator<GraphView> {

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
  public GraphView apply(GraphView graph) {
    int inEdgeCount = graph.getEdgeCount();
    int inVertexCount = graph.getVertexCount();
    int[] vertexIdMap = new int[inVertexCount];


    // builder used for vertex id candidates,
    IntArrayBuilder arrayBuilder = new IntArrayBuilder(inEdgeCount);

    // apply vertex predicate

    for (int inId = 0; inId < inVertexCount; inId++) {
      int vertexLabel = graph.getVertexLabel(inId);
      if (vertexLabelPredicate.test(vertexLabel)) {
        int outId = arrayBuilder.add(inId);
        vertexIdMap[inId] = outId;
      }
    }

    int[] outVertexIds = arrayBuilder.get();
    arrayBuilder.reset();

    // apply edge predicate and filter edges without source or target

    for (int eid = 0; eid < inEdgeCount; eid++) {
      if (edgeLabelPredicate.test(graph.getEdgeLabel(eid))
          && ArrayUtils.contains(outVertexIds, graph.getSourceId(eid))
          && ArrayUtils.contains(outVertexIds, graph.getTargetId(eid))) {
        arrayBuilder.add(eid);
      }
    }

    int[] outEdgeIds = arrayBuilder.get();
    arrayBuilder.reset();

    // create out edge data
    int outEdgeCount = outEdgeIds.length;
    int[] outEdgeLabels = new int[outEdgeCount];
    int[] outSourceIds = new int[outEdgeCount];
    int[] outTargetIds = new int[outEdgeCount];

    if (dropIsolatedVertices) {
      for (int inId : outEdgeIds) {
        arrayBuilder.add(graph.getSourceId(inId));
        arrayBuilder.add(graph.getTargetId(inId));
      }

      outVertexIds = arrayBuilder.get();
      outVertexIds = DmgmArrayUtils.distinct(outVertexIds);

      for (int outId = 0; outId < outVertexIds.length; outId++) {
        int inId = outVertexIds[outId];
        vertexIdMap[inId] = outId;
      }
    }

    int outId = 0;
    for (int inId : outEdgeIds) {
      int edgeLabel = graph.getEdgeLabel(inId);
      int sourceId = graph.getSourceId(inId);
      int targetId = graph.getTargetId(inId);

      outEdgeLabels[outId] = edgeLabel;
      outSourceIds[outId] = vertexIdMap[sourceId];
      outTargetIds[outId] = vertexIdMap[targetId];

      outId++;
    }

    // create out vertex data
    int outVertexCount = outVertexIds.length;
    int[] outVertexLabels = new int[outVertexCount];
    for (int outVertexId = 0; outVertexId < outVertexCount; outVertexId++)  {
      int inVertexId = outVertexIds[outVertexId];
      vertexIdMap[inVertexId] = outVertexId;
      outVertexLabels[outVertexId] = graph.getVertexLabel(inVertexId);
    }

    return new GraphViewBase(
      graph.getId(), graph.getLabel(), outVertexLabels, outEdgeLabels, outSourceIds, outTargetIds);
  }
}
