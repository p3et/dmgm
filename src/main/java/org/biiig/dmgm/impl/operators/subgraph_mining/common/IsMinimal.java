package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class IsMinimal implements java.util.function.Predicate<DFSCode> {

  private final InitializeParents initializeParents = new InitializeParents();
  private final GrowAllChildren growAllChildren = new GrowAllChildren();

  @Override
  public boolean test(DFSCode dfsCode ) {
    Optional<DFSCodeEmbeddingsPair> minPair = initializeParents
      .apply(dfsCode)
      .collect(new GroupByDFSCodeListEmbeddings())
      .entrySet()
      .stream()
      .map(e -> new DFSCodeEmbeddingsPair(e.getKey(), e.getValue(), 0l))
      .min(Comparator.comparing(DFSCodeEmbeddingsPair::getDFSCode));

    boolean minimal =  minPair.isPresent() && minPair.get().getDFSCode().parentOf(dfsCode);

    while (minPair.isPresent() && minimal) {
      DFSCode parentCode = minPair.get().getDFSCode();
      int[] rightmostPath = parentCode.getRightmostPath();
      Collection<DFSEmbedding> parentEmbeddings = minPair.get().getEmbeddings();

      minPair = parentEmbeddings
        .stream()
        .flatMap(e -> Stream.of(growAllChildren.apply(dfsCode, parentCode, rightmostPath, e)))
        .collect(new GroupByDFSCodeListEmbeddings())
        .entrySet()
        .stream()
        .map(e -> new DFSCodeEmbeddingsPair(e.getKey(), e.getValue(), 0l))
        .min(Comparator.comparing(DFSCodeEmbeddingsPair::getDFSCode));

      if (minPair.isPresent())
        minimal = minPair.get().getDFSCode().parentOf(dfsCode);
    }

    return minimal;
  }
}
