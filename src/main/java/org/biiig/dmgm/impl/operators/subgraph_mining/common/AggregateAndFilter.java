package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface AggregateAndFilter {
  <K, V extends WithGraphId> Stream<Pair<Pair<K, List<V>>, Map<Integer, Long>>> aggregateAndFilter(
    Stream<Pair<K, V>> reports);
}
