package org.biiig.dmgspan.gspan;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by peet on 12.07.17.
 */
public class SearchTreeNode implements Comparable<SearchTreeNode> {

  private final Comparator<DFSCode> comparator = new LexicographicalComparator();
  private final DFSCode dfsCode;
  private GraphEmbeddings[] embeddings;

  public SearchTreeNode(DFSCode dfsCode, GraphEmbeddings embeddings) {
    this.dfsCode = dfsCode;
    this.embeddings = new GraphEmbeddings[] {embeddings};
  }

  public DFSCode getDfsCode() {
    return dfsCode;
  }

  @Override
  public String toString() {
    return dfsCode + "s=" + embeddings.length;
  }

  public static void aggregateForGraph(List<SearchTreeNode> list) {
    list.sort(SearchTreeNode::compareTo);

    Iterator<SearchTreeNode> iterator = list.iterator();

    SearchTreeNode last = iterator.next();

    while (iterator.hasNext()) {
      SearchTreeNode next = iterator.next();

      if (last.getDfsCode().equals(next.getDfsCode())) {
        last.getEmbeddings()[0].merge(next.getEmbeddings()[0]);
        iterator.remove();
      } else {
        last = next;
      }
    }
  }

  @Override
  public int compareTo(SearchTreeNode that) {
    return comparator.compare(this.dfsCode, that.dfsCode);
  }

  public GraphEmbeddings[] getEmbeddings() {
    return embeddings;
  }

  public static void aggregate(List<SearchTreeNode> list) {
    list.sort(SearchTreeNode::compareTo);

    Iterator<SearchTreeNode> iterator = list.iterator();

    SearchTreeNode last = iterator.next();

    while (iterator.hasNext()) {
      SearchTreeNode next = iterator.next();

      if (last.getDfsCode().equals(next.getDfsCode())) {
        last.merge(next);
        iterator.remove();
      } else {
        last = next;
      }
    }
  }

  private void merge(SearchTreeNode that) {
    this.embeddings = ArrayUtils.addAll(this.embeddings, that.embeddings);
  }
}
