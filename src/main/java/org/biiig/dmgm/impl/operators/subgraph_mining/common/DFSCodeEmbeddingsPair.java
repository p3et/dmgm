package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.Collection;

public class DFSCodeEmbeddingsPair implements Comparable<DFSCodeEmbeddingsPair>, Supportable {
  private final DFSCode dfsCode;
  private final long support;
  private Collection<DFSEmbedding> embeddings;

  public DFSCodeEmbeddingsPair(DFSCode dfsCode, Collection<DFSEmbedding> embeddings, long support) {
    this.dfsCode = dfsCode;
    this.embeddings = embeddings;
    this.support = support;
  }

  @Override
  public DFSCode getDFSCode() {
    return dfsCode;
  }

  @Override
  public Collection<DFSEmbedding> getEmbeddings() {
    return embeddings;
  }

  @Override
  public long getSupport() {
    return support;
  }

  @Override
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
