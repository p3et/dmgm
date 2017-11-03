package org.biiig.dmgm.impl.model.collection;

import com.google.common.collect.Maps;
import org.biiig.dmgm.api.model.collection.LabelDictionary;
import org.biiig.dmgm.impl.model.countable.Countable;
import org.biiig.dmgm.impl.model.countable.CountableAscendingComparator;

import java.util.List;
import java.util.Map;

public class InMemoryLabelDictionary implements LabelDictionary {

  private Map<String, Integer> stringInteger = Maps.newHashMap();
  private Map<Integer, String> integerString = Maps.newHashMap();

  public InMemoryLabelDictionary(List<Countable<String>> labelFrequencies) {
    labelFrequencies.sort(new CountableAscendingComparator<>());

    int translation = 0;
    for (Countable<String> labelFrequency : labelFrequencies) {
      String label = labelFrequency.getObject();
      stringInteger.put(label, translation);
      integerString.put(translation, label);
      translation++;
    }
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
  public int translate(String value) {
    return stringInteger.get(value);
  }

  @Override
  public String translate(int value) {
    return integerString.get(value);
  }

}
