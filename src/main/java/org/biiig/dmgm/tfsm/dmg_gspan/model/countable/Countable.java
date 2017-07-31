package org.biiig.dmgm.tfsm.dmg_gspan.model.countable;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Countable<T extends Comparable<T>> implements Comparable<Countable<T>> {
  private final T object;
  private int support;
  private int frequency;

  public Countable(T object) {
    this.object = object;
    this.frequency = 1;
    this.support = 1;
  }

  public Countable(T object, int support, int frequency) {
    this.object = object;
    this.support = support;
    this.frequency = frequency;
  }

  public static <T extends Comparable<T>>  void aggregateFrequency(List<Countable<T>> list) {
    aggregate(list, false);
  }

  public static <T extends Comparable<T>>  Countable<T>[] aggregateFrequency(Countable<T>[] array) {
    return aggregate(array, false);
  }

  public static <T extends Comparable<T>>  Countable<T>[] aggregateSupport(Countable<T>[] array) {
    return aggregate(array, true);
  }

  public static <T extends Comparable<T>>  void aggregateSupport(
    List<Countable<T>> list) {
    aggregate(list, true);
  }

  private static <T extends Comparable<T>> void aggregate(
    List<Countable<T>> list, boolean includeSupport) {

    if (list.size() > 1) {
      list.sort(new CountableObjectComparator<>());

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

  private static <T extends Comparable<T>> Countable<T>[] aggregate(Countable<T>[] array, boolean
    includeSupport) {

    Countable<T>[] newArray;

    if (array.length > 1) {
      newArray = (Countable<T>[]) Array.newInstance(Countable.class, 1);
      Arrays.sort(array);

      Countable<T> last = array[0];
      newArray[0] = last;

      for (int i = 1; i < array.length; i++) {
        Countable<T> next = array[i];

        if (last.getObject().equals(next.getObject())) {
          last.setFrequency(last.getFrequency() + next.getFrequency());
          if (includeSupport) {
            last.setSupport(last.getSupport() + next.getSupport());
          }
        } else {
          last = next;
          newArray = ArrayUtils.add(newArray, last);
        }
      }
    } else {
      newArray = array;
    }

    return newArray;
  }

//  private static <T extends Comparable<T>> Countable<T>[] aggregate(
//    Countable<T>[] list, Comparator<Countable<T>> comparator, boolean includeSupport) {
//
//    if (list.size() > 1) {
//      list.sort(new CountableObjectComparator<>());
//
//      Iterator<Countable<T>> iterator = list.iterator();
//
//      Countable<T> last = iterator.next();
//
//      while (iterator.hasNext()) {
//        Countable<T> next = iterator.next();
//
//        if (last.getObject().equals(next.getObject())) {
//          last.setFrequency(last.getFrequency() + next.getFrequency());
//          if (includeSupport) {
//            last.setSupport(last.getSupport() + next.getSupport());
//          }
//          iterator.remove();
//        } else {
//          last = next;
//        }
//      }
//    }
//  }

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
    return "{" + object + ", " +
      "s=" + support +
      ", " +
      "f=" +
      frequency + '}';
  }


  @Override
  public int compareTo(Countable<T> that) {
    return this.getObject().compareTo(that.getObject());
  }
}
