package org.biiig.dmgm.impl.algorithms.tfsm;

import org.apache.commons.lang3.tuple.Pair;
import org.biiig.dmgm.impl.model.graph.DFSCode;

import java.util.Iterator;
import java.util.List;

public class Aggregator {

  public List<DFSTreeNode> aggregate(List<DFSTreeNode> list) {
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

    return list;
  }

  public List<DFSTreeNode> aggregateReports(List<DFSCodeEmbeddingPair> list) {
    if (list.size() > 1) {
      list.sort(DFSCodeEmbeddingPair::compareTo);

      Iterator<DFSCodeEmbeddingPair> iterator = list.iterator();

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
    return list;
  }



}
