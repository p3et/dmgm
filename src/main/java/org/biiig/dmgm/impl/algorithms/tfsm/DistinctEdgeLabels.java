package org.biiig.dmgm.impl.algorithms.tfsm;

import com.google.common.collect.Sets;
import org.biiig.dmgm.cli.StringGraph;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class DistinctEdgeLabels implements Function<StringGraph, Stream<String>> {
  
  @Override
  public Stream<String> apply(StringGraph stringGraph) {
    Set<String> set = Sets.newHashSet();
    
    for (int i = 0; i < stringGraph.getEdgeCount(); i++) 
      set.add(stringGraph.getEdgeLabel(i));
    
    return set.stream();
  }
}
