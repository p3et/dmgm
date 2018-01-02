package org.biiig.dmgm.impl.algorithms.tfsm;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.impl.graph.DFSCode;

public class GrowAllChildren {

  private final GrowChildrenByOutgoingEdges growChildrenByOutgoingEdges = new GrowChildrenByOutgoingEdges();
  private final GrowChildrenByIncomingEdges growChildrenByIncomingEdges = new GrowChildrenByIncomingEdges();


  public DFSCodeEmbeddingPair[] apply(
    Graph graph, DFSCode parent, int[] rightmostPath, DFSEmbedding parentEmbedding) {

    DFSCodeEmbeddingPair[] children =
      growChildrenByOutgoingEdges.apply(graph, parent, rightmostPath, parentEmbedding);

    children = ArrayUtils
      .addAll(children, growChildrenByIncomingEdges.apply(graph, parent, rightmostPath, parentEmbedding));

    return children;
  }


}
