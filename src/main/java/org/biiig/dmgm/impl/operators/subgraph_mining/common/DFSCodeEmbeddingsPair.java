package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.Collection;

public class DFSCodeEmbeddingsPair implements Comparable<DFSCodeEmbeddingsPair>, Supportable {
  private final DFSCode dfsCode;
  private Collection<DFSEmbedding> embeddings;

  public DFSCodeEmbeddingsPair(DFSCode dfsCode, Collection<DFSEmbedding> embeddings) {
    this.dfsCode = dfsCode;
    this.embeddings = embeddings;
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
  public int getSupport() {
    return (int) embeddings.stream().map(e -> e.getGraphId()).distinct().count();
  }

  @Override
  public int getEmbeddingCount() {
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
