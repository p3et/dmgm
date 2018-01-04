package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Optional;

class IntTaxonomy extends TaxonomyBase<Integer, int[]> {

  IntTaxonomy(int root) {
    super(root);
  }


  @Override
  public Optional<int[]> getRootPathTo(Integer child) {

    Integer parent = childParent.get(child);

    Optional<int[]> optional;
    if (parent != null) {

      // create path from special to general
      int[] path = new int[] {child};

      // a path to root is guaranteed at taxonomy creation
      while (!parent.equals(root)) {
        path = ArrayUtils.add(path, parent);
        child = parent;
        parent = childParent.get(child);
      }

      // from general to special
      ArrayUtils.reverse(path);

      optional = Optional.of(path);
    } else {
      optional = Optional.empty();
    }

    return optional;
  }

}
