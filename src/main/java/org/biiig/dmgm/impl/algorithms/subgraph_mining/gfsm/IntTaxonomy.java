package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Optional;

class IntTaxonomy extends TaxonomyBase<Integer> {

  public Optional<int[]> getPathTo(int child) {
    Integer parent = childParent.get(child);

    int[] path;

    if (parent == null) {
      path = new int[] {child};
    } else {
      path = new int[] {parent, child};

      child = parent;
      parent = childParent.get(child);

      while (parent != null) {
        path = ArrayUtils.add(path, parent);
        child = parent;
        parent = childParent.get(child);
      }
    }

    return Optional.of(path);
  }
}
