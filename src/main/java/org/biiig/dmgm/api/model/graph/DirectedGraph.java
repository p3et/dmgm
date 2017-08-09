package org.biiig.dmgm.api.model.graph;

/**
 * A directed graph.
 */
public interface DirectedGraph {
  int[] getVertexData(int vertexId);
  void setVertex(int vertexId, int label);
  void setVertex(int vertexId, int[] labels);

  int[] getEdgeData(int edgeId);
  void setEdge(int edgeId, int sourceId, int targetId, int label);
  void setEdge(int edgeId, int sourceId, int targetId, int[] labels);

  int getSourceId(int edgeId);
  int getTargetId(int edgeId);

  int[] getOutgoingEdgeIds(int vertexId);
  int[] getIncomingEdgeIds(int vertexId);

  int getVertexCount();
  int getEdgeCount();

  void trim();
}
