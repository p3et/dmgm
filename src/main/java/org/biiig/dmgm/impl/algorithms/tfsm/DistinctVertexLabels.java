package org.biiig.dmgm.impl.algorithms.tfsm;

import com.google.common.collect.Sets;
import org.biiig.dmgm.api.Graph;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class DistinctVertexLabels implements Function<Graph, Stream<Integer>> {

  @Override
  public Stream<Integer> apply(Graph graph) {
    Set<Integer> set = Sets.newHashSet();

    for (int i = 0; i < graph.getVertexCount(); i++)
      set.add(graph.getVertexLabel(i));

    return set.stream();
  }
}
