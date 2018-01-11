package org.biiig.dmgm.impl.algorithms.subgraph_mining.common;

import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class IsMinimal implements java.util.function.Predicate<DFSCodeEmbeddingsPair> {

  private final InitializeParents initializeParents = new InitializeParents();
  private final GrowAllChildren growAllChildren = new GrowAllChildren();

  @Override
  public boolean test(DFSCodeEmbeddingsPair dfsCodeEmbeddingsPair) {
    DFSCode dfsCode = dfsCodeEmbeddingsPair.getPattern();

    Optional<DFSCodeEmbeddingsPair> minPair = initializeParents
      .apply(dfsCode)
      .collect(new GroupByDFSCodeListEmbeddings())
      .entrySet()
      .stream()
      .map(e -> new DFSCodeEmbeddingsPair(e.getKey(), e.getValue()))
      .min(Comparator.comparing(DFSCodeEmbeddingsPair::getPattern));

    boolean minimal =  minPair.isPresent() && minPair.get().getPattern().parentOf(dfsCode);

    while (minPair.isPresent() && minimal) {
      DFSCode parentCode = minPair.get().getPattern();
      int[] rightmostPath = parentCode.getRightmostPath();
      Collection<DFSEmbedding> parentEmbeddings = minPair.get().getEmbeddings();

      minPair = parentEmbeddings
        .stream()
        .flatMap(e -> Stream.of(growAllChildren.apply(dfsCode, parentCode, rightmostPath, e)))
        .collect(new GroupByDFSCodeListEmbeddings())
        .entrySet()
        .stream()
        .map(e -> new DFSCodeEmbeddingsPair(e.getKey(), e.getValue()))
        .min(Comparator.comparing(DFSCodeEmbeddingsPair::getPattern));

      if (minPair.isPresent())
        minimal = minPair.get().getPattern().parentOf(dfsCode);
    }

    return minimal;
  }
}
