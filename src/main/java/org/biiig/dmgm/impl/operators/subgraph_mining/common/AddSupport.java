package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class AddSupport implements Function<Map.Entry<DFSCode,List<DFSEmbedding>>, DFSCodeEmbeddingsPair> {

  @Override
  public DFSCodeEmbeddingsPair apply(Map.Entry<DFSCode, List<DFSEmbedding>> entry) {
    long support = entry
      .getValue()
      .stream()
      .mapToLong(DFSEmbedding::getGraphId)
      .distinct()
      .count();

    return new DFSCodeEmbeddingsPair(entry.getKey(), entry.getValue(), support);
  }
}
