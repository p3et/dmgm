package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.impl.operators.fsm.common.DFSCode;
import org.biiig.dmgm.impl.operators.fsm.common.DFSEmbedding;

import java.util.Collection;

public class DFSCodeEmbeddingsPair implements Comparable<DFSCodeEmbeddingsPair> {
  private final DFSCode dfsCode;
  private Collection<DFSEmbedding> embeddings;

  public DFSCodeEmbeddingsPair(DFSCode dfsCode, Collection<DFSEmbedding> embeddings) {
    this.dfsCode = dfsCode;
    this.embeddings = embeddings;
  }

  public DFSCode getDFSCode() {
    return dfsCode;
  }

  public Collection<DFSEmbedding> getEmbeddings() {
    return embeddings;
  }

  public int getFrequency() {
    return embeddings.size();
  }

  @Override
  public int compareTo(DFSCodeEmbeddingsPair that) {
    return this.dfsCode.compareTo(that.dfsCode);
  }

  public void add(DFSEmbedding embedding) {
    this.embeddings.add(embedding);
  }

  @Override
  public String toString() {
    return "(" + dfsCode + ", " + embeddings + ')';
  }
}
