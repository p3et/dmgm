package org.biiig.dmgm.impl.operators.subgraph_mining.frequent;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.operators.subgraph.FilterVerticesAndEdgesByLabel;
import org.biiig.dmgm.impl.operators.subgraph_mining.characteristic.PreprocessorBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DistinctEdgeLabels;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DistinctVertexLabels;

import java.util.Set;

public class FrequentLabels extends PreprocessorBase {

  public FrequentLabels(float minSupport) {
    super(minSupport);
  }

  @Override
  public GraphCollection apply(GraphCollection collection, GraphCollectionBuilder builder) {
    Integer minSupportAbsolute = Math.round(collection.size() * minSupport);

    Set<Integer> frequentVertexLabels =
      getFrequentLabels(collection.stream(), new DistinctVertexLabels(), minSupportAbsolute);

    GraphCollection vertexPrunedCollection = builder.create();
    collection
        .stream()
        .map(new FilterVerticesAndEdgesByLabel(new GraphBaseFactory(), frequentVertexLabels::contains, i -> true, true))
        .forEach(vertexPrunedCollection::add);

    Set<Integer> frequentEdgeLabels =
      getFrequentLabels(vertexPrunedCollection.stream(), new DistinctEdgeLabels(), minSupportAbsolute);

    GraphCollection edgePrunedCollection = builder.create();
    vertexPrunedCollection
      .stream()
      .map(new FilterVerticesAndEdgesByLabel(new GraphBaseFactory(), i -> true, frequentEdgeLabels::contains, true))
      .forEach(edgePrunedCollection::add);

    return edgePrunedCollection;
  }
}
