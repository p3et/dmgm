package org.biiig.dmgm.impl.graph;

import org.apache.commons.lang3.ArrayUtils;

public class AdjacencyList extends CachedGraphBase {

  private final int[][] outgoingEdgeIds;
  private final int[][] incomingEdgeIds;

  public AdjacencyList(long id, int label, int[] vertexLabels, int[] edgeLabels, int[] sourceIds, int[] targetIds) {
    super(id, label, vertexLabels, edgeLabels, sourceIds, targetIds);

    int vertexCount = getVertexCount();
    outgoingEdgeIds = new int[vertexCount][];
    incomingEdgeIds = new int[vertexCount][];

    vertexIdStream()
      .forEach(vertexId -> {
        outgoingEdgeIds[vertexId] = super.getOutgoingEdgeIds(vertexId);
        incomingEdgeIds[vertexId] = super.getIncomingEdgeIds(vertexId);
      });
  }

  @Override
  public int[] getOutgoingEdgeIds(int vertexId) {
    return outgoingEdgeIds[vertexId];
  }

  @Override
  public int[] getIncomingEdgeIds(int vertexId) {
    return incomingEdgeIds[vertexId];
  }

  @Override
  public String toString() {
    return super.toString() +
      "\nO=" + ArrayUtils.toString(outgoingEdgeIds) +
      "\nI=" + ArrayUtils.toString(incomingEdgeIds) ;
  }
}
