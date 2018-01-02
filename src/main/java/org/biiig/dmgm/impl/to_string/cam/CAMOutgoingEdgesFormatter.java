package org.biiig.dmgm.impl.to_string.cam;

import org.biiig.dmgm.api.LabelDictionary;
import org.biiig.dmgm.api.Graph;

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
  protected int getAdjacentVertexId(Graph graph, int edgeId) {
    return graph.getTargetId(edgeId);
  }

  @Override
  protected int[] getEdgeIds(Graph graph, int vertexId) {
    return graph.getOutgoingEdgeIds(vertexId);
  }
}
