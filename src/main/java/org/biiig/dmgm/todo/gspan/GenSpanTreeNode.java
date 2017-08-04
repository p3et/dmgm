package org.biiig.dmgm.todo.gspan;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.impl.model.graph.DFSCode;
import org.biiig.dmgm.todo.model.Vector;
import org.biiig.dmgm.impl.model.countable.Countable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by peet on 13.07.17.
 */
public class GenSpanTreeNode implements Comparable<GenSpanTreeNode> {

  private final DFSCode dfsCode;
  private final int[] fieldMapping;
  private List<Countable<Vector>> vectors;
  private GraphDFSEmbeddings[] embeddings;

  public GenSpanTreeNode(DFSCode dfsCode, int[] fieldMapping, Vector vector,
    GraphDFSEmbeddings embeddings) {
    this.dfsCode = dfsCode;
    this.fieldMapping = fieldMapping;
    this.vectors = Lists.newLinkedList();
    this.vectors.add(new Countable<>(vector));
    this.embeddings = new GraphDFSEmbeddings[] {embeddings};
  }

  public static void aggregateForGraph(List<GenSpanTreeNode> list) {
    if (list.size() > 1) {
      list.sort(GenSpanTreeNode::compareTo);

      Iterator<GenSpanTreeNode> iterator = list.iterator();

      GenSpanTreeNode last = iterator.next();

      while (iterator.hasNext()) {
        GenSpanTreeNode next = iterator.next();

        if (last.getDfsCode().equals(next.getDfsCode())) {
          last.getEmbeddings()[0].merge(next.getEmbeddings()[0]);
          iterator.remove();
        } else {
          last = next;
        }
      }

      for (GenSpanTreeNode vectors : list) {
        Countable.sumFrequency(vectors.vectors);
      }
    }
  }

  public static void aggregate(List<GenSpanTreeNode> list) {
    if (list.size() > 1) {
      list.sort(GenSpanTreeNode::compareTo);

      Iterator<GenSpanTreeNode> iterator = list.iterator();

      GenSpanTreeNode last = iterator.next();

      while (iterator.hasNext()) {
        GenSpanTreeNode next = iterator.next();

        if (last.getDfsCode().equals(next.getDfsCode())) {
          last.merge(next);
          iterator.remove();
        } else {
          last = next;
        }
      }

      for (GenSpanTreeNode vectors : list) {
        Countable.sumSupportAndFrequency(vectors.vectors);
      }
    }
  }

  private void merge(GenSpanTreeNode that) {
    this.embeddings = ArrayUtils.addAll(this.embeddings, that.embeddings);
    this.vectors.addAll(that.vectors);
  }


  public DFSCode getDfsCode() {
    return dfsCode;
  }

  public GraphDFSEmbeddings[] getEmbeddings() {
    return embeddings;
  }


  @Override
  public int compareTo(GenSpanTreeNode that) {
    return this.dfsCode.compareTo(that.dfsCode);
  }

  @Override
  public String toString() {
    return dfsCode + Arrays.toString(fieldMapping) + vectors;
  }

  public List<Countable<Vector>> getVectors() {
    return vectors;
  }

  public void setVectors(List<Countable<Vector>> vectors) {
    this.vectors = vectors;
  }

  public int getSupport() {
    return embeddings.length;
  }

  public int getFrequency() {
    return 0;
  }

  public int[] getFieldMapping() {
    return fieldMapping;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    GenSpanTreeNode that = (GenSpanTreeNode) o;

    return dfsCode.equals(that.dfsCode);
  }

  @Override
  public int hashCode() {
    return dfsCode.hashCode();
  }
}
