package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DistinctEdgeLabels;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DistinctVertexLabels;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CharacteristicLabels extends PreprocessorBase {

  CharacteristicLabels(float minSupport) {
    super(minSupport);
  }

  @Override
  public List<CachedGraph> apply(List<CachedGraph> collection, GraphDB db) {
    Set<Integer> frequentVertexLabels = getCategoryFrequentLabels(collection, new DistinctVertexLabels());

    List<CachedGraph> vertexPrunedCollection = collection
      .stream()
//      .map(new PruneVertices(frequentVertexLabels))
      .collect(Collectors.toList());

    Set<Integer> frequentEdgeLabels =
      getCategoryFrequentLabels(vertexPrunedCollection, new DistinctEdgeLabels());

    return vertexPrunedCollection
      .stream()
//      .map(new PruneEdges(frequentEdgeLabels))
      .collect(Collectors.toList());
  }

  private Set<Integer> getCategoryFrequentLabels(List<CachedGraph> collection, Function<CachedGraph, Stream<Integer>> labelSelector) {
    Map<Integer, List<CachedGraph>> categorizedGraphs = collection
      .stream()
      .collect(new GroupByKeyListValues<>(CachedGraph::getLabel, Function.identity()));

    return categorizedGraphs
      .values()
      .stream()
      .map(g -> getFrequentLabels(g.stream(), labelSelector, (int) (g.size() * minSupport)))
      .flatMap(Collection::stream)
      .collect(Collectors.toSet());
  }

}
