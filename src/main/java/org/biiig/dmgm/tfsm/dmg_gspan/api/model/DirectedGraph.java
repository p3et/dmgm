package org.biiig.dmgm.tfsm.dmg_gspan.api.model;

/**
 * A directed graph.
 */
public interface DirectedGraph {
  int[] getVertexData(int vertexId);
  void setVertex(int vertexId, int[] data);

  int[] getEdgeData(int edgeId);
  void setEdge(int edgeId, int sourceId, int targetId, int[] data);

  int getSourceId(int edgeId);
  int getTargetId(int edgeId);

  int[] getOutgoingEdgeIds(int vertexId);
  int[] getIncomingEdgeIds(int vertexId);

  int getVertexCount();
  int getEdgeCount();
}
