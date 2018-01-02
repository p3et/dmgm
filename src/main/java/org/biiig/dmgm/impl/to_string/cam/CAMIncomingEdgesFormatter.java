package org.biiig.dmgm.impl.to_string.cam;

import org.biiig.dmgm.api.LabelDictionary;
import org.biiig.dmgm.api.Graph;

public class CAMIncomingEdgesFormatter extends CAMAdjacentEdgesFormatter {

  public CAMIncomingEdgesFormatter(
    LabelDictionary vertexDictionary, LabelDictionary edgeDictionary) {
    super(vertexDictionary, edgeDictionary);
  }

  @Override
  protected String formatEdge(String edgeLabelsString) {
    return INCOMING + edgeLabelsString + EDGE_START_END;
  }

  @Override
  protected int getAdjacentVertexId(Graph graph, int edgeId) {
    return graph.getSourceId(edgeId);
  }

  @Override
  protected int[] getEdgeIds(Graph graph, int vertexId) {
    return graph.getIncomingEdgeIds(vertexId);
  }
}
