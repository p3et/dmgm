package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import javafx.util.Pair;
import org.biiig.dmgm.impl.model.DFSCode;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSEmbedding;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AddCategorySupport implements Function<Map.Entry<DFSCode, List<DFSEmbedding>>, Pair<DFSCode, Map<Integer, Long>>> {

  private final Map<Long, int[]> graphCategories;

  public AddCategorySupport(Map<Long, int[]> graphCategories) {
    this.graphCategories = graphCategories;
  }

  @Override
  public Pair<DFSCode, Map<Integer, Long>> apply(Map.Entry<DFSCode, List<DFSEmbedding>> entry) {
    Map<Integer, Long> categorySupport = entry
      .getValue()
      .stream()
      .map(DFSEmbedding::getGraphId)
      .map(graphCategories::get)
      .flatMapToInt(IntStream::of)
      .boxed()
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    return new Pair<>(entry.getKey(), categorySupport);
  }
}
