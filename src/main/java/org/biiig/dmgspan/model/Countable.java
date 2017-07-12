package org.biiig.dmgspan.model;

import java.util.Iterator;
import java.util.List;

/**
 * Created by peet on 12.07.17.
 */
public class Countable<T extends Comparable<T>> {
  private final T object;
  private int support;
  private int frequency;

  public Countable(T object) {
    this.object = object;
    this.frequency = 1;
    this.support = 1;
  }

  public static <T extends Comparable<T>>  void aggregateFrequency(
    List<Countable<T>> list) {
    aggregate(list, false);
  }

  public static <T extends Comparable<T>>  void aggregateSupport(
    List<Countable<T>> list) {
    aggregate(list, true);
  }



  private static <T extends Comparable<T>> void aggregate(
    List<Countable<T>> list, boolean includeSupport) {

    if (list.size() > 1) {
      list.sort(new WithCountValueComparator<>());

      Iterator<Countable<T>> iterator = list.iterator();

      Countable<T> last = iterator.next();

      while (iterator.hasNext()) {
        Countable<T> next = iterator.next();

        if (last.getObject().equals(next.getObject())) {
          last.setFrequency(last.getFrequency() + next.getFrequency());
          if (includeSupport) {
            last.setSupport(last.getSupport() + next.getSupport());
          }
          iterator.remove();
        } else {
          last = next;
        }
      }
    }
  }

  public T getObject() {
    return object;
  }

  public int getSupport() {
    return support;
  }

  public void setSupport(int support) {
    this.support = support;
  }

  public int getFrequency() {
    return frequency;
  }

  public void setFrequency(int frequency) {
    this.frequency = frequency;
  }


  @Override
  public String toString() {
    return "{" + object + ", s=" + support + ", f=" + frequency + '}';
  }
}
