package org.biiig.dmgm.impl.to_string.cam;

import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.CachedGraph;

public class CAMIncomingEdgesFormatter extends CAMAdjacentEdgesFormatter {

  public CAMIncomingEdgesFormatter(GraphDB db) {
    super(db);
  }

  @Override
  protected String formatEdge(String edgeLabelsString) {
    return INCOMING + edgeLabelsString + EDGE_START_END;
  }

  @Override
  protected int getAdjacentVertexId(CachedGraph graph, int edgeId) {
    return graph.getSourceId(edgeId);
  }

  @Override
  protected int[] getEdgeIds(CachedGraph graph, int vertexId) {
    return graph.getIncomingEdgeIds(vertexId);
  }
}
