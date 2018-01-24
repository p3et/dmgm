package org.biiig.dmgm.impl.statistics;

import org.biiig.dmgm.api.GraphCollection;

import java.util.Map;
import java.util.stream.Collectors;

public interface Support<T> {
  Map<T, Integer> getAbsolute(GraphCollection collection);

  default Map<T, Double> getRelative(GraphCollection collection) {
    return getAbsolute(collection)
      .entrySet()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey, e -> (double) e.getValue() / collection.size()));
  }
}
