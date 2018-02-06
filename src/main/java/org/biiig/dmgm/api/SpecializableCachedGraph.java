package org.biiig.dmgm.api;

public interface SpecializableCachedGraph extends CachedGraph {
  int[] getTaxonomyTail(int vertexId);
}
