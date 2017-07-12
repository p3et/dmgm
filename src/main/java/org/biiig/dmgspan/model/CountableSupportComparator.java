package org.biiig.dmgspan.model;

import java.util.Comparator;

public class CountableSupportComparator<T extends Comparable<T>>
  implements Comparator<Countable<T>> {

  @Override
  public int compare(Countable<T> o1, Countable<T> o2) {
    int comparison = o2.getSupport() - o1.getSupport();

    comparison = comparison == 0 ? o2.getFrequency() - o1.getFrequency() : comparison;

    return comparison;
  }
}
