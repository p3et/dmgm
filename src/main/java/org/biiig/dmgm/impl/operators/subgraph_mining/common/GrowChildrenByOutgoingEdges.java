package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.api.SmallGraph;

public class GrowChildrenByOutgoingEdges extends GrowChildrenBase {

  @Override
  protected int[] getEdgeIds(SmallGraph graph, int fromId) {
    return graph.getOutgoingEdgeIds(fromId);
  }

  @Override
  protected int getToId(SmallGraph graph, int edgeId) {
    return graph.getTargetId(edgeId);
  }

  @Override
  protected boolean isOutgoing() {
    return true;
  }

}
