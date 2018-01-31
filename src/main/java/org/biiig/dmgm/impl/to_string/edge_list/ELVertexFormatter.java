package org.biiig.dmgm.impl.to_string.edge_list;

import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.CachedGraph;

public class ELVertexFormatter {

  private final GraphDB vertexDictionary;

  public ELVertexFormatter(GraphDB db) {
    this.vertexDictionary = db;
  }

  public String format(CachedGraph graph, int vertexId) {
    return "(" + vertexId + ":" + vertexDictionary.decode(graph.getVertexLabel(vertexId)) + ")";
  }
}
