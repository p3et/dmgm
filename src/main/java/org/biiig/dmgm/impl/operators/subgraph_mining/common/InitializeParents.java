package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.function.Function;
import java.util.stream.Stream;

public class InitializeParents implements Function<Graph, Stream<DFSCodeEmbeddingPair>> {

  @Override
  public Stream<DFSCodeEmbeddingPair> apply(Graph graph) {

    int edgeCount = graph.getEdgeCount();
    DFSCodeEmbeddingPair[] pairs = new DFSCodeEmbeddingPair[edgeCount];

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

      DFSCode dfsCode = new DFSCode();
      dfsCode.addVertex(fromLabel);
      if (!loop) dfsCode.addVertex(toLabel);
      dfsCode.addEdge(fromTime, toTime, edgeLabel, outgoing);

      DFSEmbedding embedding = new DFSEmbedding(graph.getId(), fromId, edgeId, toId);

      DFSCodeEmbeddingPair codeEmbeddingPair = new DFSCodeEmbeddingPair(dfsCode, embedding);

      pairs[edgeId] = codeEmbeddingPair;
    }


    return Stream.of(pairs);
  }

}
