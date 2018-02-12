package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import javafx.util.Pair;
import org.biiig.dmgm.impl.operators.subgraph_mining.DFSCode;

import java.util.List;
import java.util.stream.Stream;

public interface SupportMethods<S> {
  <K, V extends WithGraphId> Stream<Pair<Pair<K, List<V>>, S>> aggregateAndFilter(
    Stream<Pair<K, V>> reports);

  long[] output(List<Pair<Pair<DFSCode, List<DFSEmbedding>>, S>> filtered);
}
