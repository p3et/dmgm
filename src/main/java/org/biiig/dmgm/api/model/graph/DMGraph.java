package org.biiig.dmgm.api.model.graph;

/**
 * A directed graph.
 */
public interface DMGraph {
  int[] getVertexData(int vertexId);
  void setVertex(int vertexId, int[] data);

  int getVertexLabel(int vertexId);
  void setVertex(int vertexId, int label);

  int[] getEdgeData(int edgeId);
  void setEdge(int edgeId, int sourceId, int targetId, int[] data);

  int getEdgeLabel(int edgeId);
  void setEdge(int edgeId, int sourceId, int targetId, int label);

  int getSourceId(int edgeId);
  int getTargetId(int edgeId);

  int[] getOutgoingEdgeIds(int vertexId);
  int[] getIncomingEdgeIds(int vertexId);

  int getVertexCount();
  int getEdgeCount();

  void trim();

}
