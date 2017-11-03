package org.biiig.dmgm.impl.algorithms.tfsm.logic;

import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSTreeNode;

import java.util.Iterator;
import java.util.List;

public class DFSTreeNodeAggregator {

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

}
