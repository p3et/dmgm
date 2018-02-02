package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import javafx.util.Pair;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.lang.reflect.Array;
import java.util.function.Function;
import java.util.stream.Stream;

public class InitializeParents implements Function<CachedGraph, Stream<Pair<DFSCode,DFSEmbedding>>> {

  private final int label;

  public InitializeParents(int label) {
    this.label = label;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Stream<Pair<DFSCode,DFSEmbedding>> apply(CachedGraph graph) {

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
