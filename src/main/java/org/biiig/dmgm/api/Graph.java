package org.biiig.dmgm.api;

/**
 * A directed graph with integer labels.
 */
public interface Graph {

  int getVertexCount();
  int getEdgeCount();

  void addVertex(int label);
  void addEdge(int sourceId, int targetId, int label);

  int getVertexLabel(int vertexId);
  int getEdgeLabel(int edgeId);
  int getSourceId(int edgeId);
  int getTargetId(int edgeId);

  int[] getOutgoingEdgeIds(int vertexId);
  int[] getIncomingEdgeIds(int vertexId);

  int getId();

  void setId(int graphId);
}
