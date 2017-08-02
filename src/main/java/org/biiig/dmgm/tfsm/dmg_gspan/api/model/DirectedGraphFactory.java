package org.biiig.dmgm.tfsm.dmg_gspan.api.model;

/**
 * A factory to create directed graphs.
 */
public interface DirectedGraphFactory {
  DirectedGraph create(int vertexCount, int edgeCount);
  DirectedGraph convert(DirectedGraph graph);
}
