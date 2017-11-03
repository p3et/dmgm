package org.biiig.dmgm.impl.algorithms.tfsm.logic;

import org.biiig.dmgm.api.model.graph.DMGraph;

public class OutgoingEdgePatternGrower extends PatternGrowerBase {

  @Override
  protected int[] getEdgeIds(DMGraph graph, int fromId) {
    return graph.getOutgoingEdgeIds(fromId);
  }

  @Override
  protected int getToId(DMGraph graph, int edgeId) {
    return graph.getTargetId(edgeId);
  }

  @Override
  protected boolean isOutgoing() {
    return true;
  }

}
