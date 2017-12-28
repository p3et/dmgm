package org.biiig.dmgm.api.model.graph;

/**
 * A factory to create directed graphs.
 */
public interface IntGraphFactory {
  IntGraph create(int vertexCount, int edgeCount);
  IntGraph convert(IntGraph graph);
}
