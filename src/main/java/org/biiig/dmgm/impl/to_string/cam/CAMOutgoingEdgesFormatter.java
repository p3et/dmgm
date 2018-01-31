package org.biiig.dmgm.impl.to_string.cam;

import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.CachedGraph;

public class CAMOutgoingEdgesFormatter extends CAMAdjacentEdgesFormatter {

  public CAMOutgoingEdgesFormatter(GraphDB db) {
    super(db);
  }

  @Override
  protected String formatEdge(String edgeLabelsString) {
    return EDGE_START_END + edgeLabelsString + OUTGOING;
  }

  @Override
  protected int getAdjacentVertexId(CachedGraph graph, int edgeId) {
    return graph.getTargetId(edgeId);
  }

  @Override
  protected int[] getEdgeIds(CachedGraph graph, int vertexId) {
    return graph.getOutgoingEdgeIds(vertexId);
  }
}
