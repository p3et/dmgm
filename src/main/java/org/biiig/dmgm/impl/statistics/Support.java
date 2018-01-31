package org.biiig.dmgm.impl.statistics;

import org.biiig.dmgm.api.GraphDB;

import java.util.Map;
import java.util.stream.Collectors;

public interface Support<T> {
  Map<T, Integer> getAbsolute(GraphDB db, long collectionId);

  default Map<T, Double> getRelative(GraphDB db, long collectionId) {
    return getAbsolute(db, collectionId)
      .entrySet()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey, e -> (double) e.getValue() / db.getCachedCollection(collectionId).size()));
  }
}
