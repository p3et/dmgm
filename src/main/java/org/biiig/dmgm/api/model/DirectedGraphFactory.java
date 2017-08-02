package org.biiig.dmgm.api.model;

/**
 * A factory to create directed graphs.
 */
public interface DirectedGraphFactory {
  DirectedGraph create(int vertexCount, int edgeCount);
  DirectedGraph convert(DirectedGraph graph);
}
