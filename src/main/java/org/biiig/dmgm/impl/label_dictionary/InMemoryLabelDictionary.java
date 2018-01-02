package org.biiig.dmgm.impl.label_dictionary;

import com.google.common.collect.Maps;
import org.biiig.dmgm.api.LabelDictionary;

import java.util.Map;

public class InMemoryLabelDictionary implements LabelDictionary {

  private Map<String, Integer> stringInteger = Maps.newHashMap();
  private Map<Integer, String> integerString = Maps.newHashMap();
  private int maxTranslation = -1;


  public InMemoryLabelDictionary() {
  }

  @Override
  public String toString() {
    return integerString.toString();
  }

  @Override
  public int size() {
    return stringInteger.size();
  }

  @Override
  public int translate(String label) {
    Integer translation = stringInteger.get(label);

    if (translation == null) {
      maxTranslation++;
      stringInteger.put(label, maxTranslation);
      integerString.put(maxTranslation, label);
      translation = maxTranslation;
    }

    return translation;
  }

  @Override
  public String translate(int value) {
    return integerString.get(value);
  }

}
