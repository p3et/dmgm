package org.biiig.dmgm.api.model.graph;

/**
 * A factory to create directed graphs.
 */
public interface DirectedGraphFactory {
  DirectedGraph create(int vertexCount, int edgeCount);
  DirectedGraph convert(DirectedGraph graph);
}
