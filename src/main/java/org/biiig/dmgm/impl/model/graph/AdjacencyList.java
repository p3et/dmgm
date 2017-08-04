package org.biiig.dmgm.impl.model.graph;

import org.apache.commons.lang3.ArrayUtils;

public class AdjacencyList extends SourceTargetMux {

  private int[][] outgoingEdgeIds;
  private int[][] incomingEdgeIds;

  AdjacencyList(int edgeCount, int vertexCount) {
    super(edgeCount, vertexCount);
    incomingEdgeIds = new int[vertexCount][];
    outgoingEdgeIds = new int[vertexCount][];

    for (int vertexId = 0; vertexId < vertexCount; vertexId++) {
      incomingEdgeIds[vertexId] = new int[0];
      outgoingEdgeIds[vertexId] = new int[0];
    }
  }

  @Override
  public void setEdge(int edgeId, int sourceId, int targetId, int[] data) {
    super.setEdge(edgeId, sourceId, targetId, data);
    outgoingEdgeIds[sourceId] = ArrayUtils.add(outgoingEdgeIds[sourceId], edgeId);
    incomingEdgeIds[targetId] = ArrayUtils.add(incomingEdgeIds[targetId], edgeId);
  }

  @Override
  public int[] getOutgoingEdgeIds(int vertexId) {
    return outgoingEdgeIds[vertexId];
  }

  @Override
  public int[] getIncomingEdgeIds(int vertexId) {
    return incomingEdgeIds[vertexId];
  }
}
