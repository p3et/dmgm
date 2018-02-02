package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class GrowChildrenOf implements Function<DFSEmbedding, Stream<Pair<DFSCode,DFSEmbedding>>> {

  private final GrowChildrenByOutgoingEdges growChildrenByOutgoingEdges = new GrowChildrenByOutgoingEdges();
  private final GrowChildrenByIncomingEdges growChildrenByIncomingEdges = new GrowChildrenByIncomingEdges();
  private final DFSCode parent;
  private final int[] rightmostPaath;
  private final Map<Long, CachedGraph> input;

  public GrowChildrenOf(DFSCode parent, Map<Long, CachedGraph> input) {
    this.input = input;
    this.parent = parent;
    this.rightmostPaath = parent.getRightmostPath();
  }


  public Pair<DFSCode,DFSEmbedding>[] apply(CachedGraph graph, DFSCode parent, int[] rightmostPath, DFSEmbedding parentEmbedding) {

    Pair<DFSCode,DFSEmbedding>[] children =
      growChildrenByOutgoingEdges.apply(graph, parent, rightmostPath, parentEmbedding);

    children = ArrayUtils
      .addAll(children, growChildrenByIncomingEdges.apply(graph, parent, rightmostPath, parentEmbedding));

    return children;
  }

  @Override
  public Stream<Pair<DFSCode,DFSEmbedding>> apply(DFSEmbedding dfsEmbedding) {
    CachedGraph graph = input.get(dfsEmbedding.getGraphId());

    Pair<DFSCode,DFSEmbedding>[] outChildren = growChildrenByOutgoingEdges
      .apply(graph, parent, rightmostPaath, dfsEmbedding);

    Pair<DFSCode,DFSEmbedding>[] inChildren = growChildrenByIncomingEdges
      .apply(graph, parent, rightmostPaath, dfsEmbedding);

    return Stream.of(ArrayUtils.addAll(outChildren, inChildren));
  }
}
