package org.biiig.dmgm.impl.algorithms.tfsm.logic;

import org.biiig.dmgm.api.model.graph.DMGraph;

public class IncomingEdgePatternGrower extends PatternGrowerBase {

  @Override
  protected int[] getEdgeIds(DMGraph graph, int fromId) {
    return graph.getIncomingEdgeIds(fromId);
  }

  @Override
  protected int getToId(DMGraph graph, int edgeId) {
    return graph.getSourceId(edgeId);
  }

  @Override
  protected boolean isOutgoing() {
    return false;
  }

}
