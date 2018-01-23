package org.biiig.dmgm.impl.label_dictionary;

import com.google.common.collect.Maps;
import org.biiig.dmgm.api.LabelDictionary;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryLabelDictionary implements LabelDictionary {

  private Map<String, Integer> stringInteger = Maps.newConcurrentMap();
  private Map<Integer, String> integerString = Maps.newConcurrentMap();
  private AtomicInteger translation = new AtomicInteger();


  public InMemoryLabelDictionary() {
  }

  @Override
  public String toString() {
    return integerString.toString();
  }

  @Override
  public int size() {
    return translation.get();
  }

  @Override
  public int translate(String string) {
    Integer integer = stringInteger.get(string);

    if (integer == null) {
      integer = stringInteger.computeIfAbsent(string, k -> translation.getAndIncrement());
      integerString.putIfAbsent(integer, string);
    }

    return integer;
  }

  @Override
  public String translate(int integer) {
    return integerString.get(integer);
  }

}
