package org.biiig.dmgm.impl.to_string.edge_list;

import org.biiig.dmgm.api.model.collection.LabelDictionary;
import org.biiig.dmgm.api.model.graph.DMGraph;

public class ELVertexFormatter {

  private final LabelDictionary vertexDictionary;

  public ELVertexFormatter(LabelDictionary vertexDictionary) {
    this.vertexDictionary = vertexDictionary;
  }

  public String format(DMGraph graph, int vertexId) {
    return "(" + vertexId + ":" + vertexDictionary.translate(graph.getVertexLabel(vertexId)) + ")";
  }
}
