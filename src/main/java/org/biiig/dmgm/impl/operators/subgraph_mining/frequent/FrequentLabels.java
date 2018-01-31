package org.biiig.dmgm.impl.operators.subgraph_mining.frequent;

import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.impl.operators.subgraph.FilterVerticesAndEdgesByLabel;
import org.biiig.dmgm.impl.operators.subgraph_mining.characteristic.PreprocessorBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DistinctEdgeLabels;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DistinctVertexLabels;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FrequentLabels extends PreprocessorBase {

  public FrequentLabels(float minSupport) {
    super(minSupport);
  }

  @Override
  public List<CachedGraph> apply(List<CachedGraph> collection, GraphDB builder) {
    Integer minSupportAbsolute = Math.round(collection.size() * minSupport);

    Set<Integer> frequentVertexLabels =
      getFrequentLabels(collection.stream(), new DistinctVertexLabels(), minSupportAbsolute);

    List<CachedGraph> vertexPrunedCollection = collection
        .stream()
        .map(new FilterVerticesAndEdgesByLabel(frequentVertexLabels::contains, i -> true, true))
        .collect(Collectors.toList());

    Set<Integer> frequentEdgeLabels =
      getFrequentLabels(vertexPrunedCollection.stream(), new DistinctEdgeLabels(), minSupportAbsolute);

    return vertexPrunedCollection
      .stream()
      .map(new FilterVerticesAndEdgesByLabel(i -> true, frequentEdgeLabels::contains, true))
      .collect(Collectors.toList());
  }
}
