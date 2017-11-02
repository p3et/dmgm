package org.biiig.dmgm.impl.algorithms.tfsm;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class GraphDFSEmbeddings {
  private int graphId;
  private DFSEmbedding[] embeddings;

  public GraphDFSEmbeddings(int graphId, DFSEmbedding embedding) {
    this.graphId = graphId;
    this.embeddings = new DFSEmbedding[] {embedding};
  }

  @Override
  public String toString() {
    return StringUtils.join(embeddings, ",");
  }

  public void merge(GraphDFSEmbeddings that) {
    this.embeddings = ArrayUtils.addAll(this.embeddings, that.embeddings);
  }

  public int size() {
    return embeddings.length;
  }

  public int getGraphId() {
    return graphId;
  }

  public DFSEmbedding[] getEmbeddings() {
    return embeddings;
  }
}
