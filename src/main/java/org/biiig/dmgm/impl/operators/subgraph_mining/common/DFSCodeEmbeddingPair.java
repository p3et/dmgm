package org.biiig.dmgm.impl.operators.subgraph_mining.common;

public class DFSCodeEmbeddingPair {
  private DFSCode dfsCode;
  private final DFSEmbedding embedding;

  public DFSCodeEmbeddingPair(DFSCode dfsCode, DFSEmbedding embedding) {
    this.dfsCode = dfsCode;
    this.embedding = embedding;
  }

  public DFSCode getDfsCode() {
    return dfsCode;
  }

  public void setDfsCode(DFSCode dfsCode) {
    this.dfsCode = dfsCode;
  }

  public DFSEmbedding getEmbedding() {
    return embedding;
  }

}
