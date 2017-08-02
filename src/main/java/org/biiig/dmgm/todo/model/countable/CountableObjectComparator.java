package org.biiig.dmgm.todo.model.countable;

import java.util.Comparator;

public class CountableObjectComparator<T extends Comparable<T>> implements Comparator<Countable<T>> {
  @Override
  public int compare(Countable<T> o1, Countable<T> o2) {
    return o1.getObject().compareTo(o2.getObject());
  }
}
