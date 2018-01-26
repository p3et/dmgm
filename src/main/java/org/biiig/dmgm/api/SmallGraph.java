package org.biiig.dmgm.api;

import java.util.stream.IntStream;

/**
 * A directed graph with integer labels.
 */
public interface SmallGraph {
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

  IntStream vertexIdStream();
  IntStream edgeIdStream();

  String toString(HyperVertexDB db);
}
