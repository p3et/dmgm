package org.biiig.dmgm.api;

import java.util.stream.IntStream;

/**
 * A directed graph with integer labels.
 */
public interface Graph {
  int getLabel();
  void setLabel(int label);

  int getVertexCount();
  int getEdgeCount();

  int addVertex(int label);
  int addEdge(int sourceId, int targetId, int label);

  int getVertexLabel(int vertexId);
  int getEdgeLabel(int edgeId);
  int getSourceId(int edgeId);
  int getTargetId(int edgeId);

  int[] getOutgoingEdgeIds(int vertexId);
  int[] getIncomingEdgeIds(int vertexId);

  int getId();
  void setId(int graphId);

  IntStream vertexIdStream();

  IntStream edgeIdStream();
}
