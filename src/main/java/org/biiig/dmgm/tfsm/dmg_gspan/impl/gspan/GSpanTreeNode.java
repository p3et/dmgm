package org.biiig.dmgm.tfsm.dmg_gspan.impl.gspan;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Iterator;
import java.util.List;

/**
 * Created by peet on 12.07.17.
 */
public class GSpanTreeNode implements Comparable<GSpanTreeNode> {
  private final DFSCode dfsCode;
  private GraphDFSEmbeddings[] embeddings;

  public GSpanTreeNode(DFSCode dfsCode, GraphDFSEmbeddings embeddings) {
    this.dfsCode = dfsCode;
    this.embeddings = new GraphDFSEmbeddings[] {embeddings};
  }





  public static void aggregateForGraph(List<GSpanTreeNode> list) {
    if (list.size() > 1) {
      list.sort(GSpanTreeNode::compareTo);

      Iterator<GSpanTreeNode> iterator = list.iterator();

      GSpanTreeNode last = iterator.next();

      while (iterator.hasNext()) {
        GSpanTreeNode next = iterator.next();

        if (last.getDfsCode().equals(next.getDfsCode())) {
          last.getEmbeddings()[0].merge(next.getEmbeddings()[0]);
          iterator.remove();
        } else {
          last = next;
        }
      }
    }
  }

  public static void aggregate(List<GSpanTreeNode> list) {
    if (list.size() > 1) {
      list.sort(GSpanTreeNode::compareTo);

      Iterator<GSpanTreeNode> iterator = list.iterator();

      GSpanTreeNode last = iterator.next();

      while (iterator.hasNext()) {
        GSpanTreeNode next = iterator.next();

        if (last.getDfsCode().equals(next.getDfsCode())) {
          last.merge(next);
          iterator.remove();
        } else {
          last = next;
        }
      }
    }
  }

  private void merge(GSpanTreeNode that) {
    this.embeddings = ArrayUtils.addAll(this.embeddings, that.embeddings);
  }

  public DFSCode getDfsCode() {
    return dfsCode;
  }

  public GraphDFSEmbeddings[] getEmbeddings() {
    return embeddings;
  }


  @Override
  public int compareTo(GSpanTreeNode that) {
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
