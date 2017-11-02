package org.biiig.dmgm.impl.algorithms.tfsm;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.impl.model.graph.DFSCode;

/**
 * Created by peet on 12.07.17.
 */
public class DFSTreeNode implements Comparable<DFSTreeNode> {
  private final DFSCode dfsCode;
  private GraphDFSEmbeddings[] embeddings;

  public DFSTreeNode(DFSCode dfsCode, GraphDFSEmbeddings embeddings) {
    this.dfsCode = dfsCode;
    this.embeddings = new GraphDFSEmbeddings[] {embeddings};
  }

  public DFSCode getDfsCode() {
    return dfsCode;
  }

  public GraphDFSEmbeddings[] getEmbeddings() {
    return embeddings;
  }

  public void merge(DFSTreeNode that) {
    this.embeddings = ArrayUtils.addAll(this.embeddings, that.embeddings);
  }

  @Override
  public int compareTo(DFSTreeNode that) {
    return this.dfsCode.compareTo(that.dfsCode);
  }

  @Override
  public String toString() {
    return dfsCode + "s=" + embeddings.length;
  }

  public int getSupport() {
    return embeddings.length;
  }

  public int getFrequency() {
    int frequency = 0;

    for (GraphDFSEmbeddings embeddings : embeddings) {
      frequency += embeddings.size();
    }

    return frequency;
  }
}
