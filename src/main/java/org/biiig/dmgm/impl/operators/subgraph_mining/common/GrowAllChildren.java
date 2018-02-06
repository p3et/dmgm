package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import javafx.util.Pair;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.api.SpecializableCachedGraph;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class GrowAllChildren implements Function<Pair<DFSCode,List<DFSEmbedding>>, Stream<Pair<DFSCode,DFSEmbedding>>> {
  private final Map<Long, SpecializableCachedGraph> input;

  public GrowAllChildren(Map<Long, SpecializableCachedGraph> input) {
    this.input = input;
  }

  @Override
  public Stream<Pair<DFSCode,DFSEmbedding>> apply(Pair<DFSCode,List<DFSEmbedding>> dfsCodeEmbeddingsPair) {
    DFSCode parent = dfsCodeEmbeddingsPair.getKey();

    return dfsCodeEmbeddingsPair
      .getValue()
      .stream()
      .flatMap(new GrowChildrenOf(parent, input));
  }
}