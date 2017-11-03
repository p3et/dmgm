package org.biiig.dmgm.impl.algorithms.tfsm.model;

import org.biiig.dmgm.impl.model.graph.DFSCode;

public class DFSCodeEmbeddingPair implements Comparable<DFSCodeEmbeddingPair> {
  private final DFSCode dfsCode;
  private DFSEmbedding embedding;

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

  @Override
  public int compareTo(DFSCodeEmbeddingPair that) {
    return this.dfsCode.compareTo(that.dfsCode);
  }
}
