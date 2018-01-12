package org.biiig.dmgm.impl.operators.subgraph_mining.frequent;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.GraphCollectionBuilder;
import org.biiig.dmgm.impl.operators.subgraph_mining.characteristic.PreprocessorBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DistinctEdgeLabels;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DistinctVertexLabels;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.PruneEdges;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.PruneVertices;

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
        .map(new PruneVertices(frequentVertexLabels))
        .forEach(vertexPrunedCollection::add);

    Set<Integer> frequentEdgeLabels =
      getFrequentLabels(vertexPrunedCollection.stream(), new DistinctEdgeLabels(), minSupportAbsolute);

    GraphCollection edgePrunedCollection = builder.create();
    vertexPrunedCollection
      .stream()
      .map(new PruneEdges(frequentEdgeLabels))
      .forEach(edgePrunedCollection::add);

    return edgePrunedCollection;
  }
}
