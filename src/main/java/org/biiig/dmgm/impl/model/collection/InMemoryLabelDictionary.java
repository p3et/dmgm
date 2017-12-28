package org.biiig.dmgm.impl.model.collection;

import com.google.common.collect.Maps;
import org.biiig.dmgm.api.model.collection.LabelDictionary;
import org.biiig.dmgm.impl.model.countable.Countable;
import org.biiig.dmgm.impl.model.countable.CountableAscendingComparator;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class InMemoryLabelDictionary implements LabelDictionary {

  private Map<String, Integer> stringInteger = Maps.newHashMap();
  private Map<Integer, String> integerString = Maps.newHashMap();
  private int maxTranslation = -1;

  public InMemoryLabelDictionary(List<Countable<String>> labelFrequencies) {
    labelFrequencies.sort(new CountableAscendingComparator<>());

    for (Countable<String> labelFrequency : labelFrequencies) {
      maxTranslation++;
      String label = labelFrequency.getObject();
      stringInteger.put(label, maxTranslation);
      integerString.put(maxTranslation, label);
    }
  }

  public InMemoryLabelDictionary(Collection<String> labels) {
    for (String label : labels) {
      if (!stringInteger.containsKey(label)) {
        maxTranslation++;
        stringInteger.put(label, maxTranslation);
        integerString.put(maxTranslation, label);
      }
    }
  }

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
