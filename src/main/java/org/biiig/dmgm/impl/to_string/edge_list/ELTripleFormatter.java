package org.biiig.dmgm.impl.to_string.edge_list;

import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.CachedGraph;

public class ELTripleFormatter {

  private final ELVertexFormatter vertexFormatter;
  private final ELEdgeFormatter edgeFormatter;

  public ELTripleFormatter(GraphDB db) {
    vertexFormatter = new ELVertexFormatter(db);
    edgeFormatter = new ELEdgeFormatter(db);

  }

  public String format(CachedGraph graph, int edgeId) {
    String sourceString = vertexFormatter.format(graph, graph.getSourceId(edgeId));
    String edgeString = edgeFormatter.format(graph, edgeId);
    String targetString = vertexFormatter.format(graph, graph.getTargetId(edgeId));
    return sourceString + edgeString + targetString;
  }
}
