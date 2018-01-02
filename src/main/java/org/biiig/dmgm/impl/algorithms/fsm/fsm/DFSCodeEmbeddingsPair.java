package org.biiig.dmgm.impl.algorithms.fsm.fsm;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.impl.graph.DFSCode;

public class DFSCodeEmbeddingsPair implements Comparable<DFSCodeEmbeddingsPair> {
  private final DFSCode dfsCode;
  private DFSEmbedding[] embeddings;

  public DFSCodeEmbeddingsPair(DFSCode dfsCode, DFSEmbedding embedding) {
    this.dfsCode = dfsCode;
    this.embeddings = new DFSEmbedding[] {embedding};
  }

  public DFSCodeEmbeddingsPair(DFSCode dfsCode, DFSEmbedding[] embeddings) {
    this.dfsCode = dfsCode;
    this.embeddings = embeddings;
  }

  public DFSCode getDfsCode() {
    return dfsCode;
  }

  public DFSEmbedding[] getEmbeddings() {
    return embeddings;
  }

  @Override
  public int compareTo(DFSCodeEmbeddingsPair that) {
    return this.dfsCode.compareTo(that.dfsCode);
  }

  public void add(DFSEmbedding embedding) {
    this.embeddings = ArrayUtils.add(this.embeddings, embedding);
  }
}
