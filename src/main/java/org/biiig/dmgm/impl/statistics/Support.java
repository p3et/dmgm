package org.biiig.dmgm.impl.statistics;

import org.biiig.dmgm.api.HyperVertexDB;

import java.util.Map;
import java.util.stream.Collectors;

public interface Support<T> {
  Map<T, Integer> getAbsolute(HyperVertexDB db, long hyperVertexId);

  default Map<T, Double> getRelative(HyperVertexDB db, long hyperVertexId) {
    double vertexCount = db.getElementsOf(hyperVertexId).getLeft().length;
    return getAbsolute(db, hyperVertexId)
      .entrySet()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey, e -> (double) e.getValue() / vertexCount));
  }
}
