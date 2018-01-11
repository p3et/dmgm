package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.Supportable;

import java.util.Collection;
import java.util.stream.Collectors;

class PatternVectorsPair implements Supportable {

  private final Graph dfsCode;
  private final Collection<MultiDimensionalVector> vectors;

  public PatternVectorsPair(Graph dfsCode, Collection<MultiDimensionalVector> vectors) {
    this.dfsCode = dfsCode;
    this.vectors = vectors;
  }

  @Override
  public Graph getPattern() {
    return dfsCode;
  }

  @Override
  public Collection<DFSEmbedding> getEmbeddings() {
    return vectors.stream().map(MultiDimensionalVector::getEmbedding).collect(Collectors.toList());
  }

  @Override
  public int getSupport() {
    return (int) getEmbeddings().stream().map(DFSEmbedding::getGraphId).distinct().count();
  }

  @Override
  public int getEmbeddingCount() {
    return vectors.size();
  }

  public Collection<MultiDimensionalVector> getVectors() {
    return vectors;
  }
}
