package org.biiig.dmgm.impl.operators.subgraph;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.impl.graph.CachedGraphBase;

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
    int[] vertexCandidates = graph
      .vertexIdStream()
      .filter(v -> vertexLabelPredicate.test(graph.getVertexLabel(v)))
      .toArray();

    int[] keepEdges = graph
      .edgeIdStream()
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

    int[] vertexLabels = IntStream
      .range(0, keepVertices.length)
      .map(outVertexId -> {
        int inVertexId = keepVertices[outVertexId];
        vertexIdMap[inVertexId] = outVertexId;
        return graph.getVertexLabel(inVertexId);
      })
      .toArray();

    int edgeCount = keepEdges.length;
    int[] edgeLabels = new int[edgeCount];
    int[] sourceIds = new int[edgeCount];
    int[] targetIds = new int[edgeCount];

    IntStream
      .of(keepEdges)
      .forEach(edgeId -> {
        edgeLabels[edgeId] = graph.getEdgeLabel(edgeId);
        sourceIds[edgeId] = vertexIdMap[graph.getSourceId(edgeId)];
        targetIds[edgeId] = vertexIdMap[graph.getTargetId(edgeId)];
      });

    return new CachedGraphBase(graph.getId(), graph.getLabel(), vertexLabels, edgeLabels, sourceIds, targetIds);
  }
}
