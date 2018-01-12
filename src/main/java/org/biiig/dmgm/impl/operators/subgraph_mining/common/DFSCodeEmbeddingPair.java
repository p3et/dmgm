package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.impl.graph.DFSCode;

public class DFSCodeEmbeddingPair {
  private final DFSCode dfsCode;
  private final DFSEmbedding embedding;

  public DFSCodeEmbeddingPair(DFSCode dfsCode, DFSEmbedding embedding) {
    this.dfsCode = dfsCode;
    this.embedding = embedding;
  }

  public DFSCode getDfsCode() {
    return dfsCode;
  }

  public DFSEmbedding getEmbedding() {
    return embedding;
  }

}
