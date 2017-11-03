package org.biiig.dmgm.api.model.graph;

/**
 * A factory to create directed graphs.
 */
public interface DMGraphFactory {
  DMGraph create(int vertexCount, int edgeCount);
  DMGraph convert(DMGraph graph);
}
