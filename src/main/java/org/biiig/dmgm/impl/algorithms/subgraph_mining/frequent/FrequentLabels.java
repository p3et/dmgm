package org.biiig.dmgm.impl.algorithms.subgraph_mining.frequent;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.GraphCollectionBuilder;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.Preprocessor;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.PruneEdges;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.PruneVertices;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FrequentLabels implements Preprocessor {
  private final float minSupport;

  public FrequentLabels(float minSupport) {
    this.minSupport = minSupport;
  }

  @Override
  public GraphCollection apply(GraphCollection collection, GraphCollectionBuilder builder) {

      Integer minSupportAbsolute = Math.round(collection.size() * minSupport);

      Set<Integer> frequentVertexLabels = getFrequentLabels(
        collection
          .stream()
          .flatMap(new DistinctVertexLabels()),
        minSupportAbsolute);

      GraphCollection vertexPrunedCollection = builder.create();

    collection
        .stream()
        .map(new PruneVertices(frequentVertexLabels))
        .forEach(vertexPrunedCollection::add);

      Set<Integer> frequentEdgeLabels = getFrequentLabels(
        collection
          .stream()
          .flatMap(new DistinctEdgeLabels()),
        minSupportAbsolute);

      GraphCollection prunedCollection = builder.create();

      vertexPrunedCollection
        .stream()
        .map(new PruneEdges(frequentEdgeLabels))
        .forEach(prunedCollection::add);

      return vertexPrunedCollection;
}


  private Set<Integer> getFrequentLabels(Stream<Integer> labels, Integer minSupportAbsolute) {
    return labels
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
      .entrySet()
      .stream()
      .filter(e -> e.getValue() >= minSupportAbsolute)
      .map(Map.Entry::getKey)
      .collect(Collectors.toSet());
  }
}
