package org.biiig.dmgm.api.model.graph;

/**
 * A directed graph with integer labels.
 */
public interface IntGraph {

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
}
