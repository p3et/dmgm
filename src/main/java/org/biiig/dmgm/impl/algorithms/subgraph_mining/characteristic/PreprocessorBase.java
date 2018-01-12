package org.biiig.dmgm.impl.algorithms.subgraph_mining.characteristic;

import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.Preprocessor;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class PreprocessorBase implements Preprocessor {
  protected final float minSupport;

  public PreprocessorBase(float minSupport) {
    this.minSupport = minSupport;
  }

  protected Set<Integer> getFrequentLabels(Stream<Graph> graphs, Function<Graph, Stream<Integer>> labelSelector, Integer minSupportAbsolute) {
    return graphs
          .flatMap(labelSelector)
          .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
          .entrySet()
          .stream()
          .filter(e -> e.getValue() >= minSupportAbsolute)
          .map(Map.Entry::getKey)
          .collect(Collectors.toSet());
  }
}
