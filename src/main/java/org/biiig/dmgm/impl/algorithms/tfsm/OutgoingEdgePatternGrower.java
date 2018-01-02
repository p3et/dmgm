package org.biiig.dmgm.impl.algorithms.tfsm;

import org.biiig.dmgm.api.Graph;

public class OutgoingEdgePatternGrower extends PatternGrowerBase {

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
