package org.biiig.dmgm.impl.model.countable;

import java.util.Comparator;

public class CountableDescendingComparator<T extends Comparable<T>>
  implements Comparator<Countable<T>> {

  @Override
  public int compare(Countable<T> o1, Countable<T> o2) {
    int comparison = o2.getSupport() - o1.getSupport();

    if (comparison == 0) {
      comparison = o2.getFrequency() - o1.getFrequency();

      if (comparison == 0) {
        comparison = o2.compareTo(o1);
      }
    }

    return comparison;
  }
}
