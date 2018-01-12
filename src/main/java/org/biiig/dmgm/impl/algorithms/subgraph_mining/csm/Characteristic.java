package org.biiig.dmgm.impl.algorithms.subgraph_mining.csm;

import com.google.common.collect.Lists;
import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import javafx.util.Pair;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPropertyKeys;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.Supportable;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.math.BigDecimal;
import java.util.Collection;
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
  private final float minSupport;

  Characteristic(
    Interestingness interestingness,
    Map<Integer, Integer> graphLabel,
    Map<Integer, Integer> graphLabelCounts,
    int graphCount,
    float minSupport) {
    this.graphLabel = graphLabel;
    this.graphLabelCounts = graphLabelCounts;
    this.interestingness = interestingness;
    this.graphCount = graphCount;

    this.minSupport = minSupport;
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

    Map<Integer, Float> labelSupports = categoryEmbeddings
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

    boolean atLeastOnceFrequent = false;

    for (float support : labelSupports.values())
      if (atLeastOnceFrequent = support >= minSupport)
        break;

    if (atLeastOnceFrequent) {
      child = Optional.of(supportable);

      int[] labels = interestingness.getInterestingCategories(labelSupports, totalSupport);

      if (labels != null && labels.length > 0) {
        Collection<Consumer<GraphCollection>> outputs = Lists.newArrayListWithCapacity(labels.length);

        for (int label : labels) {
          outputs.add(output -> {
            DFSCode dfsCode = supportable.getDFSCode().deepCopy();
            dfsCode.setLabel(label);
            int graphId = output.add(dfsCode);
            BigDecimal support = BigDecimal.valueOf(labelSupports.get(label));
            output.getElementDataStore()
              .setGraph(graphId, SubgraphMiningPropertyKeys.SUPPORT, support);
          });
        }

        store = Optional.of(s -> outputs.forEach(o -> o.accept(s)));

      } else {
        store = Optional.empty();
      }
    } else {
      child = Optional.empty();
      store = Optional.empty();
    }

    return new Pair<>(child, store);
  }
}
