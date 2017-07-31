package org.biiig.dmgm.tfsm.dmg_gspan.vector_mining;


import org.biiig.dmgm.tfsm.dmg_gspan.model.Vector;
import org.biiig.dmgm.tfsm.dmg_gspan.model.countable.Countable;

import java.util.List;

public class CrossLevelFrequentVectorsBottomUp extends CrossLevelFrequentVectorsBase {

  @Override
  public List<Countable<Vector>> mine(List<Countable<Vector>> vectors, int minSupport) {
    clearLists();
    setBottomNodes(vectors, false);

    children.addAll(bottom);

    result.addAll(children);

    while (!children.isEmpty()) {
      for (GFSMLatticeNode child : children) {
        int minField = child.getMinField();
        Vector vector = child.getVector();

        if (vector.getField(minField)[0] == 0) {
          minField++;
        }

        for (int field = minField; field < vector.size(); field++) {
          int level = vector.getField(field).length - 1;
          while (level > 0 && vector.getField(field)[level] == 0) {
            level--;
          }

          GFSMLatticeNode parent = child.deepCopy();
          parent.setMinField(field);
          parent.getVector().getField(field)[level] = 0;
          parents.add(parent);
        }
      }

      aggregate(parents, false);
      result.addAll(parents);
      switchParentsChildren();
      parents.clear();
    }

    result.removeIf(n -> n.getSupport() < minSupport);

    return frequentVectors();
  }


}
