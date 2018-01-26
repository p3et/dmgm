package org.biiig.dmgm.impl.statistics;

import org.biiig.dmgm.api.HyperVertexDB;
import org.biiig.dmgm.api.SmallGraph;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface Support<T> {
  Map<T, Integer> getAbsolute(HyperVertexDB db, long collectionId);

  default Map<T, Double> getRelative(HyperVertexDB db, long collectionId) {
    return getAbsolute(db, collectionId)
      .entrySet()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey, e -> (double) e.getValue() / db.getCollection(collectionId).size()));
  }
}
