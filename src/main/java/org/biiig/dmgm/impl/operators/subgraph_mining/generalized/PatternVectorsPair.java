package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeSupportablePair;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.Collection;
import java.util.stream.Collectors;

public class PatternVectorsPair implements DFSCodeSupportablePair {

  private final DFSCode dfsCode;
  private final Collection<MultiDimensionalVector> vectors;

  public PatternVectorsPair(DFSCode dfsCode, Collection<MultiDimensionalVector> vectors) {
    this.dfsCode = dfsCode;
    this.vectors = vectors;
  }

  @Override
  public DFSCode getDFSCode() {
    return dfsCode;
  }

  @Override
  public Collection<DFSEmbedding> getEmbeddings() {
    return vectors.stream().map(MultiDimensionalVector::getEmbedding).collect(Collectors.toList());
  }

  public Collection<MultiDimensionalVector> getVectors() {
    return vectors;
  }
}
