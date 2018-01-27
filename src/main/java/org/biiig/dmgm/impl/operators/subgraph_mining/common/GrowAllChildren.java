package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class GrowAllChildren implements Function<DFSCodeEmbeddingsPair, Stream<DFSCodeEmbeddingPair>> {
  private final Map<Long, SmallGraph> input;

  public GrowAllChildren(Map<Long, SmallGraph> input) {
    this.input = input;
  }

  @Override
  public Stream<DFSCodeEmbeddingPair> apply(DFSCodeEmbeddingsPair dfsCodeEmbeddingsPair) {
    DFSCode parent = dfsCodeEmbeddingsPair.getDFSCode();

    return dfsCodeEmbeddingsPair
      .getEmbeddings()
      .stream()
      .flatMap(new GrowChildrenOf(parent, input));
  }
}