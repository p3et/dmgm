package org.biiig.dmgm.impl.model.countable;

import java.util.Comparator;

public class CountableAscendingComparator<T extends Comparable<T>>
  implements Comparator<Countable<T>> {

  @Override
  public int compare(Countable<T> o1, Countable<T> o2) {
    int comparison = o1.getSupport() - o2.getSupport();

    if (comparison == 0) {
      comparison = o1.getFrequency() - o2.getFrequency();

      if (comparison == 0) {
        comparison = o1.compareTo(o2);
      }
    }

    return comparison;
  }
}
