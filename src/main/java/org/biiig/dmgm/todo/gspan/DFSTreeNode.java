package org.biiig.dmgm.todo.gspan;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.impl.model.graph.DFSCode;

import java.util.Iterator;
import java.util.List;

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

  public static void aggregateForGraph(List<DFSTreeNode> list) {
    if (list.size() > 1) {
      list.sort(DFSTreeNode::compareTo);

      Iterator<DFSTreeNode> iterator = list.iterator();

      DFSTreeNode last = iterator.next();

      while (iterator.hasNext()) {
        DFSTreeNode next = iterator.next();

        if (last.getDfsCode().equals(next.getDfsCode())) {
          last.getEmbeddings()[0].merge(next.getEmbeddings()[0]);
          iterator.remove();
        } else {
          last = next;
        }
      }
    }
  }

  public static void aggregate(List<DFSTreeNode> list) {
    if (list.size() > 1) {
      list.sort(DFSTreeNode::compareTo);

      Iterator<DFSTreeNode> iterator = list.iterator();

      DFSTreeNode last = iterator.next();

      while (iterator.hasNext()) {
        DFSTreeNode next = iterator.next();

        if (last.getDfsCode().equals(next.getDfsCode())) {
          last.merge(next);
          iterator.remove();
        } else {
          last = next;
        }
      }
    }
  }

  private void merge(DFSTreeNode that) {
    this.embeddings = ArrayUtils.addAll(this.embeddings, that.embeddings);
  }

  public DFSCode getDfsCode() {
    return dfsCode;
  }

  public GraphDFSEmbeddings[] getEmbeddings() {
    return embeddings;
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
