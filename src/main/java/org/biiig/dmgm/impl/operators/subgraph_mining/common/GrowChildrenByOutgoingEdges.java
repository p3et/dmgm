package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.api.Graph;

public class GrowChildrenByOutgoingEdges extends GrowChildrenBase {

  @Override
  protected int[] getEdgeIds(Graph graph, int fromId) {
    return graph.getOutgoingEdgeIds(fromId);
  }

  @Override
  protected int getToId(Graph graph, int edgeId) {
    return graph.getTargetId(edgeId);
  }

  @Override
  protected boolean isOutgoing() {
    return true;
  }

}
