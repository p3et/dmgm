package org.biiig.dmgm.impl.to_string.cam;

import org.biiig.dmgm.api.SmallGraph;

public class CAMOutgoingEdgesFormatter extends CAMAdjacentEdgesFormatter {

  public CAMOutgoingEdgesFormatter(
    LabelDictionary vertexDictionary, LabelDictionary edgeDictionary) {
    super(vertexDictionary);
  }

  @Override
  protected String formatEdge(String edgeLabelsString) {
    return EDGE_START_END + edgeLabelsString + OUTGOING;
  }

  @Override
  protected int getAdjacentVertexId(SmallGraph graph, int edgeId) {
    return graph.getTargetId(edgeId);
  }

  @Override
  protected int[] getEdgeIds(SmallGraph graph, int vertexId) {
    return graph.getOutgoingEdgeIds(vertexId);
  }
}
