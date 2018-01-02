package org.biiig.dmgm.impl.algorithms.tfsm;

import com.google.common.collect.Sets;
import org.biiig.dmgm.api.Graph;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class DistinctEdgeLabels implements Function<Graph, Stream<Integer>> {
  
  @Override
  public Stream<Integer> apply(Graph Graph) {
    Set<Integer> set = Sets.newHashSet();
    
    for (int i = 0; i < Graph.getEdgeCount(); i++)
      set.add(Graph.getEdgeLabel(i));
    
    return set.stream();
  }
}
