package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Optional;

class IntTaxonomy extends TaxonomyBase<Integer> {

  IntTaxonomy(int root) {
    super(root, Integer.class);
  }

  public Optional<int[]> getRootPathTo(int child) {
    return getRootPathTo(Integer.valueOf(child))
      .map(ArrayUtils::toPrimitive);
  }

}
