package org.biiig.dmgm.impl.to_string.cam;

import org.biiig.dmgm.api.model.collection.LabelDictionary;
import org.biiig.dmgm.api.model.graph.DMGraph;

public class CAMOutgoingEdgesFormatter extends CAMAdjacentEdgesFormatter {

  public CAMOutgoingEdgesFormatter(
    LabelDictionary vertexDictionary, LabelDictionary edgeDictionary) {
    super(vertexDictionary, edgeDictionary);
  }

  @Override
  protected String formatEdge(String edgeLabelsString) {
    return EDGE_START_END + edgeLabelsString + OUTGOING;
  }

  @Override
  protected int getAdjacentVertexId(DMGraph graph, int edgeId) {
    return graph.getTargetId(edgeId);
  }

  @Override
  protected int[] getEdgeIds(DMGraph graph, int vertexId) {
    return graph.getOutgoingEdgeIds(vertexId);
  }
}
