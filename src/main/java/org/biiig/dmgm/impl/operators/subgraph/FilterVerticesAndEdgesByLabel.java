package org.biiig.dmgm.impl.operators.subgraph;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.SmallGraph;

import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

public class FilterVerticesAndEdgesByLabel implements Function<SmallGraph, SmallGraph> {
  public static final IntPredicate NO_PREDICATE = i -> true;

  private final GraphFactory graphFactory;
  private final IntPredicate vertexLabelPredicate;
  private final IntPredicate edgeLabelPredicate;
  private final Boolean dropIsolatedVertices;

  public FilterVerticesAndEdgesByLabel(GraphFactory graphFactory, IntPredicate vertexLabelPredicate, IntPredicate edgeLabelPredicate, Boolean dropIsolatedVertices) {
    this.graphFactory = graphFactory;
    this.vertexLabelPredicate = vertexLabelPredicate;
    this.edgeLabelPredicate = edgeLabelPredicate;
    this.dropIsolatedVertices = dropIsolatedVertices;
  }

  @Override
  public SmallGraph apply(SmallGraph inGraph) {
    int[] vertexCandidates = inGraph
      .vertexIdStream()
      .filter(v -> vertexLabelPredicate.test(inGraph.getVertexLabel(v)))
      .toArray();

    int[] keepEdges = inGraph
      .edgeIdStream()
      .filter(v -> edgeLabelPredicate.test(inGraph.getEdgeLabel(v)))
      .filter(e -> ArrayUtils.contains(vertexCandidates, inGraph.getSourceId(e)))
      .filter(e -> ArrayUtils.contains(vertexCandidates, inGraph.getTargetId(e)))
      .toArray();

    int[] keepVertices;

    if (dropIsolatedVertices) {
      IntStream sourceIds = IntStream
        .of(keepEdges)
        .map(inGraph::getSourceId);

      IntStream targetIds = IntStream
        .of(keepEdges)
        .map(inGraph::getTargetId);

      keepVertices = IntStream
        .concat(sourceIds, targetIds)
        .distinct()
        .filter(v -> ArrayUtils.contains(vertexCandidates, v))
        .toArray();
    } else {
      keepVertices = vertexCandidates;
    }

    int[] vertexIdMap = new int[inGraph.getVertexCount()];

    SmallGraph outGraph = graphFactory.create();

    IntStream
      .range(0, keepVertices.length)
      .forEach(outVertexId -> {
        int inVertexId = keepVertices[outVertexId];
        vertexIdMap[inVertexId] = outVertexId;
        int label = inGraph.getVertexLabel(inVertexId);
        outGraph.addVertex(label);
      });

    IntStream
      .of(keepEdges)
      .forEach(edgeId -> {
        int sourceId = vertexIdMap[inGraph.getSourceId(edgeId)];
        int targetId = vertexIdMap[inGraph.getTargetId(edgeId)];
        int edgeLabel = inGraph.getEdgeLabel(edgeId);
        outGraph.addEdge(sourceId, targetId, edgeLabel);
      });

    return outGraph;
  }
}
