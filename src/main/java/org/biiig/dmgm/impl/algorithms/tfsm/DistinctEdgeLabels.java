package org.biiig.dmgm.impl.algorithms.tfsm;

import com.google.common.collect.Sets;
import org.biiig.dmgm.api.model.graph.IntGraph;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class DistinctEdgeLabels implements Function<IntGraph, Stream<Integer>> {
  
  @Override
  public Stream<Integer> apply(IntGraph IntGraph) {
    Set<Integer> set = Sets.newHashSet();
    
    for (int i = 0; i < IntGraph.getEdgeCount(); i++) 
      set.add(IntGraph.getEdgeLabel(i));
    
    return set.stream();
  }
}
