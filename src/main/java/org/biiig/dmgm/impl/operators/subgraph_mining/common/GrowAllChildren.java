package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class GrowAllChildren implements Function<DFSCodeEmbeddingsPair, Stream<DFSCodeEmbeddingPair>> {

  private final GrowChildrenByOutgoingEdges growChildrenByOutgoingEdges = new GrowChildrenByOutgoingEdges();
  private final GrowChildrenByIncomingEdges growChildrenByIncomingEdges = new GrowChildrenByIncomingEdges();


  public DFSCodeEmbeddingPair[] apply(SmallGraph graph, DFSCode parent, int[] rightmostPath, DFSEmbedding parentEmbedding) {

    DFSCodeEmbeddingPair[] children =
      growChildrenByOutgoingEdges.apply(graph, parent, rightmostPath, parentEmbedding);

    children = ArrayUtils
      .addAll(children, growChildrenByIncomingEdges.apply(graph, parent, rightmostPath, parentEmbedding));

    return children;
  }


  @Override
  public Stream<DFSCodeEmbeddingPair> apply(DFSCodeEmbeddingsPair dfsCodeEmbeddingsPair) {
    return null;
  }
}
