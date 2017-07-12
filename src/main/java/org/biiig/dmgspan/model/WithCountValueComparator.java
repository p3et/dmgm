package org.biiig.dmgspan.model;

import java.util.Comparator;

/**
 * Created by peet on 12.07.17.
 */
public class WithCountValueComparator<T extends Comparable<T>> implements Comparator<Countable<T>> {
  @Override
  public int compare(Countable<T> o1, Countable<T> o2) {
    return o1.getObject().compareTo(o2.getObject());
  }
}
