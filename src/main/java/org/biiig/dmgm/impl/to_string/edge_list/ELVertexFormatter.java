package org.biiig.dmgm.impl.to_string.edge_list;

import org.biiig.dmgm.api.LabelDictionary;
import org.biiig.dmgm.api.Graph;

public class ELVertexFormatter {

  private final LabelDictionary vertexDictionary;

  public ELVertexFormatter(LabelDictionary vertexDictionary) {
    this.vertexDictionary = vertexDictionary;
  }

  public String format(Graph graph, int vertexId) {
    return "(" + vertexId + ":" + vertexDictionary.translate(graph.getVertexLabel(vertexId)) + ")";
  }
}
