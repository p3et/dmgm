package org.biiig.dmgm.impl.graph;

import org.apache.commons.lang3.ArrayUtils;

public class AdjacencyList extends SmallGraphBase {

  private int[][] outgoingEdgeIds = new int[0][];
  private int[][] incomingEdgeIds = new int[0][];

  public AdjacencyList() {
    super(id, label, vertexLabels, edgeLabels, sourceIds, targetIds);
  }

  @Override
  public int addVertex(int label) {
    super.addVertex(label);

    outgoingEdgeIds = ArrayUtils.add(outgoingEdgeIds, new int[0]);
    incomingEdgeIds = ArrayUtils.add(incomingEdgeIds, new int[0]);
    return label;
  }

  @Override
  public int addEdge(int sourceId, int targetId, int label) {
    super.addEdge(sourceId, targetId, label);

    int edgeId = getEdgeCount() - 1;
    outgoingEdgeIds[sourceId] = ArrayUtils.add(outgoingEdgeIds[sourceId], edgeId);
    incomingEdgeIds[targetId] = ArrayUtils.add(incomingEdgeIds[targetId], edgeId);
    return edgeId;
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
