package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import com.google.common.collect.Sets;
import org.biiig.dmgm.api.CachedGraph;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class DistinctEdgeLabels implements Function<CachedGraph, Stream<Integer>> {
  
  @Override
  public Stream<Integer> apply(CachedGraph Graph) {
    Set<Integer> set = Sets.newHashSet();
    
    for (int i = 0; i < Graph.getEdgeCount(); i++)
      set.add(Graph.getEdgeLabel(i));
    
    return set.stream();
  }
}
