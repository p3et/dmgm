package org.biiig.dmgm.impl.to_string.cam;

import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.CachedGraph;

public class CAMVertexFormatter {

  private final GraphDB db;
  private final CAMOutgoingEdgesFormatter outgoingEdgesFormatter;
  private final CAMIncomingEdgesFormatter incomingEdgesFormatter;

  public CAMVertexFormatter(GraphDB db) {
    this.db = db;
    this.outgoingEdgesFormatter = new CAMOutgoingEdgesFormatter(db);
    this.incomingEdgesFormatter = new CAMIncomingEdgesFormatter(db);
  }

  public String format(CachedGraph graph, int vertexId) {
    return db.decode(graph.getVertexLabel(vertexId)) +
      outgoingEdgesFormatter.format(graph, vertexId) +
      incomingEdgesFormatter.format(graph, vertexId);
  }

}
