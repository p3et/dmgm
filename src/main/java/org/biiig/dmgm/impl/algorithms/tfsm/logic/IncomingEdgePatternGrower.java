package org.biiig.dmgm.impl.algorithms.tfsm.logic;

import org.biiig.dmgm.api.model.graph.IntGraph;

public class IncomingEdgePatternGrower extends PatternGrowerBase {

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
