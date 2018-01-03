package org.biiig.dmgm.impl.algorithms.subgraph_mining.common;

import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class IsMinimal implements java.util.function.Predicate<DFSCodeEmbeddingsPair> {

  private final InitializeParents initializeParents = new InitializeParents();
  private final GrowAllChildren growAllChildren = new GrowAllChildren();

  @Override
  public boolean test(DFSCodeEmbeddingsPair dfsCodeEmbeddingsPair) {
    DFSCode dfsCode = dfsCodeEmbeddingsPair.getDfsCode();

    Optional<DFSCodeEmbeddingsPair> minPair = initializeParents
      .apply(dfsCode)
      .collect(new GroupByDFSCodeArrayEmbeddings())
      .entrySet()
      .stream()
      .map(e -> new DFSCodeEmbeddingsPair(e.getKey(), e.getValue()))
      .min(Comparator.comparing(DFSCodeEmbeddingsPair::getDfsCode));

    boolean minimal =  minPair.isPresent() && minPair.get().getDfsCode().parentOf(dfsCode);

    while (minPair.isPresent() && minimal) {
      DFSCode parentCode = minPair.get().getDfsCode();
      int[] rightmostPath = parentCode.getRightmostPath();
      DFSEmbedding[] parentEmbeddings = minPair.get().getEmbeddings();

      minPair = Stream
        .of(parentEmbeddings)
        .flatMap(e -> Stream.of(growAllChildren.apply(dfsCode, parentCode, rightmostPath, e)))
        .collect(new GroupByDFSCodeArrayEmbeddings())
        .entrySet()
        .stream()
        .map(e -> new DFSCodeEmbeddingsPair(e.getKey(), e.getValue()))
        .min(Comparator.comparing(DFSCodeEmbeddingsPair::getDfsCode));

      if (minPair.isPresent())
        minimal = minPair.get().getDfsCode().parentOf(dfsCode);
    }

    return minimal;
  }
}
