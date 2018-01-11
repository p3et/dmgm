package org.biiig.dmgm.impl.algorithms.subgraph_mining.csm;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import javafx.util.Pair;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPropertyKeys;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.Supportable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Characteristic<T extends Supportable> implements FilterOrOutput<T> {
  private final Map<Integer, Integer> graphLabel;
  private final Map<Integer, Integer> graphLabelCounts;

  private final Interestingness interestingness;
  private final int graphCount;

  Characteristic(
    Interestingness interestingness,
    Map<Integer, Integer> graphLabel,
    Map<Integer, Integer> graphLabelCounts,
    int graphCount
  ) {
    this.graphLabel = graphLabel;
    this.graphLabelCounts = graphLabelCounts;
    this.interestingness = interestingness;
    this.graphCount = graphCount;
  }

  @Override
  public Pair<Optional<T>, Optional<Consumer<GraphCollection>>> apply(T supportable) {

    Map<Integer, List<DFSEmbedding>> categoryEmbeddings = supportable
      .getEmbeddings()
      .stream()
      .collect(new GroupByKeyListValues<>(
        e -> graphLabel.get(e.getGraphId()),
        Function.identity())
      );

    Map<Integer, Float> categorySupports = categoryEmbeddings
      .entrySet()
      .stream()
      .collect(Collectors.toMap(
        Map.Entry::getKey,
        e -> (float) e.getValue()
          .stream()
          .map(DFSEmbedding::getGraphId)
          .distinct()
          .count()
          / graphLabelCounts.get(e.getKey())));

    float totalSupport = (float) supportable.getSupport() / graphCount;

    Optional<T> child;
    Optional<Consumer<GraphCollection>> store;

    int[] labels = interestingness.getInterestingCategories(categorySupports, totalSupport);

    if (labels.length > 0) {
      child = Optional.of(supportable);
      store = Optional.of(s -> {
        int graphId = s.add(supportable.getDFSCode());
        s.getElementDataStore()
          .setGraph(graphId, SubgraphMiningPropertyKeys.SUPPORT, supportable.getSupport());
        s.getElementDataStore()
          .setGraph(graphId, SubgraphMiningPropertyKeys.EMBEDDING_COUNT, supportable.getEmbeddingCount());
        s.getElementDataStore()
          .setGraph(graphId, SubgraphMiningPropertyKeys.CHARACTERISTIC_FOR, labels);
      });

    } else {
      child = Optional.empty();
      store = Optional.empty();
    }

    return new Pair<>(child, store);
  }
}
