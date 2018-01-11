package org.biiig.dmgm.impl.algorithms.subgraph_mining.common;

import org.biiig.dmgm.api.Graph;

import java.util.Collection;

public interface Supportable {
  Graph getPattern();
  Collection<DFSEmbedding> getEmbeddings();
  int getSupport();
  int getEmbeddingCount();
}
