package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class GrowChildrenOf implements Function<DFSEmbedding, Stream<DFSCodeEmbeddingPair>> {

  private final GrowChildrenByOutgoingEdges growChildrenByOutgoingEdges = new GrowChildrenByOutgoingEdges();
  private final GrowChildrenByIncomingEdges growChildrenByIncomingEdges = new GrowChildrenByIncomingEdges();
  private final DFSCode parent;
  private final int[] rightmostPaath;
  private final Map<Long, SmallGraph> input;

  public GrowChildrenOf(DFSCode parent, Map<Long, SmallGraph> input) {
    this.input = input;
    this.parent = parent;
    this.rightmostPaath = parent.getRightmostPath();
  }


  public DFSCodeEmbeddingPair[] apply(SmallGraph graph, DFSCode parent, int[] rightmostPath, DFSEmbedding parentEmbedding) {

    DFSCodeEmbeddingPair[] children =
      growChildrenByOutgoingEdges.apply(graph, parent, rightmostPath, parentEmbedding);

    children = ArrayUtils
      .addAll(children, growChildrenByIncomingEdges.apply(graph, parent, rightmostPath, parentEmbedding));

    return children;
  }

  @Override
  public Stream<DFSCodeEmbeddingPair> apply(DFSEmbedding dfsEmbedding) {
    SmallGraph graph = input.get(dfsEmbedding.getGraphId());

    DFSCodeEmbeddingPair[] outChildren = growChildrenByOutgoingEdges
      .apply(graph, parent, rightmostPaath, dfsEmbedding);

    DFSCodeEmbeddingPair[] inChildren = growChildrenByIncomingEdges
      .apply(graph, parent, rightmostPaath, dfsEmbedding);

    return Stream.of(ArrayUtils.addAll(outChildren, inChildren));
  }
}
