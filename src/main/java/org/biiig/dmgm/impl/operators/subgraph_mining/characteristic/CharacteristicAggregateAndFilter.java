package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import javafx.util.Pair;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.AggregateAndFilter;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.WithGraphId;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CharacteristicAggregateAndFilter implements AggregateAndFilter {

  private final Map<Long, int[]> graphCategories;
  private final Map<Integer, Long> categoryMinSupport;

  public CharacteristicAggregateAndFilter(Map<Long, int[]> graphCategories, Map<Integer, Long> categoryMinSupport) {
    this.graphCategories = graphCategories;
    this.categoryMinSupport = categoryMinSupport;
  }

  @Override
  public <K, V extends WithGraphId> Stream<Pair<Pair<K, List<V>>, Map<Integer, Long>>> aggregateAndFilter(Stream<Pair<K, V>> reports) {

    return reports
      .collect(new GroupByKeyListValues<>(Pair::getKey, Pair::getValue))
      .entrySet()
      .stream()
      .map(e -> new Pair<>(
        new Pair<>(e.getKey(), e.getValue()),
        e.getValue().stream()
          .map(WithGraphId::getGraphId)
          .distinct()
          .map(graphCategories::get)
          .flatMapToInt(IntStream::of)
          .boxed()
          .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
      ))
      .filter(p -> p.getValue()
        .entrySet()
        .stream()
        .anyMatch(e -> e.getValue() >= categoryMinSupport.get(e.getKey())));}
}
