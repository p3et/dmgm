package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.Collection;
import java.util.Optional;

public interface Taxonomy<T, P> {
  void add(T parent, T child) throws InvalidArgumentException;

  /**
   * Identifies a path from a given value to the root of a taxonomy from general to special (excluding root).
   * Not path will be returned if value is not part of the taxonomy.
   *
   * @param child value
   * @return path to root without root
   */
  Optional<P> getRootPathTo(T child);

  T getParent(T child);

  Collection<T> getChildren(T a);
}
