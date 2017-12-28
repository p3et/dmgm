package org.biiig.dmgm.impl.algorithms.tfsm;

import com.google.common.collect.Lists;
import javafx.util.Pair;
import org.biiig.dmgm.api.model.graph.IntGraph;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSEmbedding;
import org.biiig.dmgm.impl.model.graph.DFSCode;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

public class SingleEdgePatterns implements Function<IntGraph, Stream<Pair<DFSCode, DFSEmbedding>>> {

  @Override
  public Stream<Pair<DFSCode, DFSEmbedding>> apply(IntGraph graph) {

    int edgeCount = graph.getEdgeCount();
    Collection<Pair<DFSCode, DFSEmbedding>> pairs = Lists.newArrayListWithExpectedSize(edgeCount);

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

      pairs.add(new Pair<>(dfsCode, embedding));
    }

//    System.out.println("------");
//    pairs
//      .stream()
//      .map(p -> p.getKey())
//      .sorted()
//      .forEach(d -> System.out.println(d));

    return pairs.stream();
  }

}
