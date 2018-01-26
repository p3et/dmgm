package org.biiig.dmgm.impl.graph;

public class AdjacencyList extends SmallGraphBase {

  private final int[][] outgoingEdgeIds;
  private final int[][] incomingEdgeIds;

  public AdjacencyList(long id, int label, int[] vertexLabels, int[] edgeLabels, int[] sourceIds, int[] targetIds) {
    super(id, label, vertexLabels, edgeLabels, sourceIds, targetIds);

    int vertexCount = getVertexCount();
    outgoingEdgeIds = new int[vertexCount][];
    incomingEdgeIds = new int[vertexCount][];

    vertexIdStream()
      .forEach(vertexId -> {
        outgoingEdgeIds[vertexId] = getOutgoingEdgeIds(vertexId);
        incomingEdgeIds[vertexId] = getIncomingEdgeIds(vertexId);
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
}
