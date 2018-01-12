package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.GraphCollectionBuilder;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DistinctEdgeLabels;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DistinctVertexLabels;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.PruneEdges;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.PruneVertices;

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
  public GraphCollection apply(GraphCollection collection, GraphCollectionBuilder builder) {
    Set<Integer> frequentVertexLabels = getCategoryFrequentLabels(collection, new DistinctVertexLabels());

    GraphCollection vertexPrunedCollection = builder.create();
    collection
      .stream()
      .map(new PruneVertices(frequentVertexLabels))
      .forEach(vertexPrunedCollection::add);

    Set<Integer> frequentEdgeLabels =
      getCategoryFrequentLabels(vertexPrunedCollection, new DistinctEdgeLabels());

    GraphCollection edgePrunedCollection = builder.create();
    vertexPrunedCollection
      .stream()
      .map(new PruneEdges(frequentEdgeLabels))
      .forEach(edgePrunedCollection::add);

    return edgePrunedCollection;
  }

  private Set<Integer> getCategoryFrequentLabels(GraphCollection collection, Function<Graph, Stream<Integer>> labelSelector) {
    Map<Integer, List<Graph>> categorizedGraphs = collection
      .stream()
      .collect(new GroupByKeyListValues<>(Graph::getLabel, Function.identity()));

    return categorizedGraphs
      .values()
      .stream()
      .map(g -> getFrequentLabels(g.stream(), labelSelector, (int) (g.size() * minSupport)))
      .flatMap(Collection::stream)
      .collect(Collectors.toSet());
  }

}
