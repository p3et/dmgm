package org.biiig.dmgm.impl.model;

import org.biiig.dmgm.api.model.DirectedGraph;

/**
 * Created by peet on 02.08.17.
 */
public abstract class DirectedGraphBase implements DirectedGraph {
  protected int[][] vertexData;
  protected int[][] edgeData;

  public DirectedGraphBase(int edgeCount, int vertexCount) {
    edgeData = new int[edgeCount][];
    vertexData = new int[vertexCount][];
  }

  @Override
  public int[] getVertexData(int vertexId) {
    return vertexData[vertexId];
  }

  @Override
  public int[] getEdgeData(int edgeId) {
    return edgeData[edgeId];
  }

  @Override
  public int getVertexCount() {
    return vertexData.length;
  }

  @Override
  public int getEdgeCount() {
    return edgeData.length;
  }
}
