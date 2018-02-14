package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import org.biiig.dmgm.impl.operators.fsm.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCode;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeEmbeddingsPair;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AddMaxCategorySupport implements Function<Map.Entry<DFSCode,List<DFSEmbedding>>, DFSCodeEmbeddingsPair> {

  private final Map<Long, int[]> graphCategories;

  public AddMaxCategorySupport(Map<Long, int[]> graphCategories) {
    this.graphCategories = graphCategories;
  }

  @Override
  public DFSCodeEmbeddingsPair apply(Map.Entry<DFSCode, List<DFSEmbedding>> entry) {
    Map<Integer, Long> categorySupports = entry
      .getValue()
      .stream()
      .map(DFSEmbedding::getGraphId)
      .map(graphCategories::get)
      .flatMapToInt(IntStream::of)
      .boxed()
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    long maxSupport = categorySupports
      .entrySet()
      .stream()
      .max(Comparator.comparing(Map.Entry::getValue))
      .get()
      .getValue();

    return new DFSCodeEmbeddingsPair(entry.getKey(), entry.getValue());
  }
}
