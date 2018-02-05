package org.biiig.dmgm.api;

/**
 * A directed graph with integer labels.
 */
public interface CachedGraph {
  long getId();
  int getLabel();

  int getVertexLabel(int vertexId);

  int getEdgeLabel(int edgeId);
  int getSourceId(int edgeId);
  int getTargetId(int edgeId);

  int[] getOutgoingEdgeIds(int vertexId);
  int[] getIncomingEdgeIds(int vertexId);

  int getVertexCount();
  int getEdgeCount();

  String toString(GraphDB db);

  int[] getVertexLabels();
  int[] getEdgeLabels();

  void setVertexLabel(int vertexId, int label);

  int[] getSourceIds();

  int[] getTargetIds();
}
