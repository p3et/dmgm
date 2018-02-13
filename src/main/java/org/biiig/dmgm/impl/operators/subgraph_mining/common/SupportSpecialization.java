package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import javafx.util.Pair;

import java.util.List;
import java.util.stream.Stream;

public interface SupportSpecialization<S> {
  <K, V extends WithGraphId> Stream<Pair<Pair<K, List<V>>, S>> aggregateAndFilter(Stream<Pair<K, V>> candidates);

  long[] output(Pair<Pair<DFSCode, List<DFSEmbedding>>, S> pair);
}
