package org.biiig.dmgm.impl.graph;

import org.biiig.dmgm.api.SpecializableCachedGraph;

public class SpecializableAdjacencyList extends AdjacencyList implements SpecializableCachedGraph {


  private final int[][] taxonomyTails;

  public SpecializableAdjacencyList(long id, int label, int[] vertexLabels, int[] edgeLabels, int[] sourceIds, int[] targetIds, int[][] taxonomyTails) {
    super(id, label, vertexLabels, edgeLabels, sourceIds, targetIds);
    this.taxonomyTails = taxonomyTails;
  }

  @Override
  public int[] getTaxonomyTail(int vertexId) {
    return taxonomyTails[vertexId];
  }
}
