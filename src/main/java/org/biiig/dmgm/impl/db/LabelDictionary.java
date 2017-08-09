package org.biiig.dmgm.impl.db;

import com.google.common.collect.Maps;
import org.biiig.dmgm.impl.model.countable.Countable;
import org.biiig.dmgm.impl.model.countable.CountableAscendingComparator;

import java.util.List;
import java.util.Map;

public class LabelDictionary {

  Map<String, Integer> stringInteger = Maps.newHashMap();
  Map<Integer, String> integerString = Maps.newHashMap();

  public LabelDictionary(List<Countable<String>> labelFrequencies) {
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

  public int size() {
    return stringInteger.size();
  }

  public Integer translate(String value) {
    return stringInteger.get(value);
  }
}
