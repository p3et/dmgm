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
