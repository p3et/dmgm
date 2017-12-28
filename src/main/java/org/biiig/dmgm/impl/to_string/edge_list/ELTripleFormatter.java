package org.biiig.dmgm.impl.to_string.edge_list;

import org.biiig.dmgm.api.model.collection.LabelDictionary;
import org.biiig.dmgm.api.model.graph.IntGraph;

public class ELTripleFormatter {

  private final ELVertexFormatter vertexFormatter;
  private final ELEdgeFormatter edgeFormatter;

  public ELTripleFormatter(LabelDictionary vertexDictionary, LabelDictionary edgeDictionary) {
    vertexFormatter = new ELVertexFormatter(vertexDictionary);
    edgeFormatter = new ELEdgeFormatter(edgeDictionary);

  }

  public String format(IntGraph graph, int edgeId) {
    String sourceString = vertexFormatter.format(graph, graph.getSourceId(edgeId));
    String edgeString = edgeFormatter.format(graph, edgeId);
    String targetString = vertexFormatter.format(graph, graph.getTargetId(edgeId));
    return sourceString + edgeString + targetString;
  }
}
