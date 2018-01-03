package org.biiig.dmgm.impl.algorithms.subgraph_mining.ccp;

import org.biiig.dmgm.api.Graph;

import java.util.function.Function;

public interface Categorization extends Function<Graph, String> {
  String categorize(Graph graph);

  @Override
  default String apply(Graph graph) {
    return categorize(graph);
  }
}
