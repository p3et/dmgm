package org.biiig.dmgm.cli.pattern_growth;

import org.biiig.dmgm.api.model.graph.IntGraph;

public class GrowChildrenByIncomingEdges extends GrowChildrenBase {

  @Override
  protected int[] getEdgeIds(IntGraph graph, int fromId) {
    return graph.getIncomingEdgeIds(fromId);
  }

  @Override
  protected int getToId(IntGraph graph, int edgeId) {
    return graph.getSourceId(edgeId);
  }

  @Override
  protected boolean isOutgoing() {
    return false;
  }

}
