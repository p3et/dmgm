package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Optional;

public class StringTaxonomy extends TaxonomyBase<String, String[]> {

  public static final String ROOT = "_";

  public StringTaxonomy() {
    super(ROOT);
  }

  @Override
  public Optional<String[]> getRootPathTo(String child) {

    String parent = childParent.get(child);

    Optional<String[]> optional;
    if (parent != null) {

      // create path from special to general
      String[] path = new String[] {child};

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
