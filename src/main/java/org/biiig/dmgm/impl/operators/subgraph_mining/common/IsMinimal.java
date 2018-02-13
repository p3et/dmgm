package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import com.google.common.collect.Maps;
import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import javafx.util.Pair;
import org.biiig.dmgm.api.model.CachedGraph;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class IsMinimal implements java.util.function.Predicate<DFSCode> {

  private final InitializeParents<CachedGraph> initializeParents = new InitializeParents<>(0);

  @Override
  public boolean test(DFSCode dfsCode ) {
    GrowChildrenOf<CachedGraph> growChildrenOf = new GrowChildrenOf<>(dfsCode, Maps.newHashMap());


    Optional<Pair<DFSCode,List<DFSEmbedding>>> minPair = initializeParents
      .apply(dfsCode)
      .collect(new GroupByKeyListValues<>(Pair::getKey, Pair::getValue))
      .entrySet()
      .stream()
      .map(e -> new Pair<>(e.getKey(), e.getValue()))
      .min(Comparator.comparing(Pair::getKey));

    boolean minimal =  minPair.isPresent() && minPair.get().getKey().parentOf(dfsCode);

    while (minPair.isPresent() && minimal) {
      DFSCode parentCode = minPair.get().getKey();
      int[] rightmostPath = parentCode.getRightmostPath();
      Collection<DFSEmbedding> parentEmbeddings = minPair.get().getValue();

      minPair = parentEmbeddings
        .stream()
        .flatMap(e -> Stream.of(growChildrenOf.apply(dfsCode, parentCode, rightmostPath, e)))
        .collect(new GroupByKeyListValues<>(Pair::getKey, Pair::getValue))
        .entrySet()
        .stream()
        .map(e -> new Pair<>(e.getKey(), e.getValue()))
        .min(Comparator.comparing(Pair::getKey));

      if (minPair.isPresent()) {
        DFSCode minCode = minPair.get().getKey();
        minimal = minCode.parentOf(dfsCode);
      }
    }

    return minimal;
  }
}
