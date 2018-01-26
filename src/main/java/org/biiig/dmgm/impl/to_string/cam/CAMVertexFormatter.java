package org.biiig.dmgm.impl.to_string.cam;

import org.biiig.dmgm.api.HyperVertexDB;
import org.biiig.dmgm.api.SmallGraph;

public class CAMVertexFormatter {

  private final HyperVertexDB db;
  private final CAMOutgoingEdgesFormatter outgoingEdgesFormatter;
  private final CAMIncomingEdgesFormatter incomingEdgesFormatter;

  public CAMVertexFormatter(HyperVertexDB db) {
    this.db = db;
    this.outgoingEdgesFormatter = new CAMOutgoingEdgesFormatter(db);
    this.incomingEdgesFormatter = new CAMIncomingEdgesFormatter(db);
  }

  public String format(SmallGraph graph, int vertexId) {
    return db.decode(graph.getVertexLabel(vertexId)) +
      outgoingEdgesFormatter.format(graph, vertexId) +
      incomingEdgesFormatter.format(graph, vertexId);
  }

}
