package org.biiig.dmgm.impl.model.graph;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.NotImplementedException;

public class AdjacencyList extends IntGraphBase {

  private int[][] outgoingEdgeIds = new int[0][];
  private int[][] incomingEdgeIds = new int[0][];

  @Override
  public void addEdge(int sourceId, int targetId, int label) {
    super.addEdge(sourceId, targetId, label);

    int edgeId = getEdgeCount() - 1;
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
