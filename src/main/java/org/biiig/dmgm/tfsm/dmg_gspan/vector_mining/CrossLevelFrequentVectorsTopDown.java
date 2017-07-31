package org.biiig.dmgm.tfsm.dmg_gspan.vector_mining;

import org.biiig.dmgm.tfsm.dmg_gspan.model.Vector;
import org.biiig.dmgm.tfsm.dmg_gspan.model.countable.Countable;

import java.util.Iterator;
import java.util.List;

public class CrossLevelFrequentVectorsTopDown extends CrossLevelFrequentVectorsBase {

  @Override
  public List<Countable<Vector>> mine(List<Countable<Vector>> vectors, int minSupport) {
    clearLists();
    setBottomNodes(vectors, true);

    parents.add(getRoot());
    result.add(getRoot());

    while (! parents.isEmpty()) {
      for (GFSMLatticeNode parent : parents) {
        int minField = parent.getMinField();
        Vector vector = parent.getVector();
        if (vector.getField(minField)[vector.getField(minField).length - 1] != 0) {
          minField++;
        }

        for (int i : parent.getSupporters()) {
          GFSMLatticeNode supporter = bottom.get(i);

          for (int field = minField; field < vector.size(); field++) {
            int level = 0;

            while (level < vector.getField(field).length - 1 && vector.getField(field)[level] != 0) {
              level++;
            }

            GFSMLatticeNode child = supporter.deepCopy();
            child.setVector(parent.getVector().deepCopy());
            child.setMinField(field);
            child.getVector().getField(field)[level] = supporter.getVector().getField(field)[level];
            children.add(child);
          }
        }
      }
      aggregate(children, true);
      children.removeIf(n -> n.getSupport() < minSupport);
      result.addAll(children);
      switchParentsChildren();
      children.clear();
    }

    return frequentVectors();
  }

  private GFSMLatticeNode getRoot() {
    Iterator<GFSMLatticeNode> iterator = bottom.iterator();

    GFSMLatticeNode root = iterator.next().deepCopy();
    root.setSupporters(new int[bottom.size()]);

    int supporter = 0;
    root.getSupporters()[supporter] = supporter;

    while (iterator.hasNext()) {
      supporter++;
      GFSMLatticeNode next = iterator.next();

      root.setSupport(root.getSupport() + next.getSupport());
      root.setFrequency(root.getFrequency() + next.getFrequency());
      root.getSupporters()[supporter] = supporter;
    }

    for (int field = 0; field < root.getVector().size(); field++) {
      for (int level = 0; level < root.getVector().getField(field).length; level++) {
        root.getVector().getField(field)[level] = 0;
      }
    }

    return root;
  }
}
