package org.biiig.dmgm.impl.to_string.cam;

import org.biiig.dmgm.api.HyperVertexDB;
import org.biiig.dmgm.api.SmallGraph;

public class CAMIncomingEdgesFormatter extends CAMAdjacentEdgesFormatter {

  public CAMIncomingEdgesFormatter(HyperVertexDB db) {
    super(db);
  }

  @Override
  protected String formatEdge(String edgeLabelsString) {
    return INCOMING + edgeLabelsString + EDGE_START_END;
  }

  @Override
  protected int getAdjacentVertexId(SmallGraph graph, int edgeId) {
    return graph.getSourceId(edgeId);
  }

  @Override
  protected int[] getEdgeIds(SmallGraph graph, int vertexId) {
    return graph.getIncomingEdgeIds(vertexId);
  }
}
