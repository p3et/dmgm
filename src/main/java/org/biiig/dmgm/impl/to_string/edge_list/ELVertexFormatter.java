package org.biiig.dmgm.impl.to_string.edge_list;

import org.biiig.dmgm.api.HyperVertexDB;
import org.biiig.dmgm.api.SmallGraph;

public class ELVertexFormatter {

  private final HyperVertexDB vertexDictionary;

  public ELVertexFormatter(HyperVertexDB db) {
    this.vertexDictionary = db;
  }

  public String format(SmallGraph graph, int vertexId) {
    return "(" + vertexId + ":" + vertexDictionary.decode(graph.getVertexLabel(vertexId)) + ")";
  }
}
