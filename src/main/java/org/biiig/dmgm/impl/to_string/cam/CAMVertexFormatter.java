package org.biiig.dmgm.impl.to_string.cam;

import org.biiig.dmgm.api.SmallGraph;

public class CAMVertexFormatter {

  private final LabelDictionary vertexDictionary;
  private final CAMOutgoingEdgesFormatter outgoingEdgesFormatter;
  private final CAMIncomingEdgesFormatter incomingEdgesFormatter;

  public CAMVertexFormatter(LabelDictionary vertexDictionary, LabelDictionary edgeDictionary) {
    this.vertexDictionary = vertexDictionary;
    this.outgoingEdgesFormatter = new CAMOutgoingEdgesFormatter(vertexDictionary, edgeDictionary);
    this.incomingEdgesFormatter = new CAMIncomingEdgesFormatter(vertexDictionary);
  }

  public String format(SmallGraph graph, int vertexId) {
    return vertexDictionary.translate(graph.getVertexLabel(vertexId)) +
      outgoingEdgesFormatter.format(graph, vertexId) +
      incomingEdgesFormatter.format(graph, vertexId);
  }

}
