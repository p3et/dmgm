package org.biiig.dmgm.cli.pattern_growth;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.model.graph.IntGraph;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSCodeEmbeddingPair;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSEmbedding;
import org.biiig.dmgm.impl.model.graph.DFSCode;

public class GrowAllChildren {

  private final GrowChildrenByOutgoingEdges growChildrenByOutgoingEdges = new GrowChildrenByOutgoingEdges();
  private final GrowChildrenByIncomingEdges growChildrenByIncomingEdges = new GrowChildrenByIncomingEdges();


  public DFSCodeEmbeddingPair[] apply(
    IntGraph graph, DFSCode parent, int[] rightmostPath, DFSEmbedding parentEmbedding) {

    DFSCodeEmbeddingPair[] children =
      growChildrenByOutgoingEdges.apply(graph, parent, rightmostPath, parentEmbedding);

    children = ArrayUtils
      .addAll(children, growChildrenByIncomingEdges.apply(graph, parent, rightmostPath, parentEmbedding));

    return children;
  }


}
