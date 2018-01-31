package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.api.CachedGraph;

public class GrowChildrenByIncomingEdges extends GrowChildrenBase {

  @Override
  protected int[] getEdgeIds(CachedGraph graph, int fromId) {
    return graph.getIncomingEdgeIds(fromId);
  }

  @Override
  protected int getToId(CachedGraph graph, int edgeId) {
    return graph.getSourceId(edgeId);
  }

  @Override
  protected boolean isOutgoing() {
    return false;
  }

}
