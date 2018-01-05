package org.biiig.dmgm.impl.algorithms.subgraph_mining.csm;

import org.biiig.dmgm.api.ElementDataStore;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.GraphCollectionBuilder;
import org.biiig.dmgm.api.LabelDictionary;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutputFactory;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningBase;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPropertyKeys;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * min frequency is with regard to f
 */
public class CharacteristicSubgraphs extends SubgraphMiningBase {

  static final String DEFAULT_CATEGORY = "_default";
  private final Interestingness interestingness;

  /**
   * @param interestingness
   * */

  public CharacteristicSubgraphs(
    float minSupportRel, int maxEdgeCount, Interestingness interestingness) {

    super(minSupportRel, maxEdgeCount);
    this.interestingness = interestingness;
  }

  @Override
  public FilterAndOutputFactory getFilterAndOutputFactory(GraphCollection input) {
    ElementDataStore dataStore = input.getElementDataStore();
    LabelDictionary dictionary = input.getLabelDictionary();
    int defaultCategory = dictionary.translate(DEFAULT_CATEGORY);

    Map<Integer, Integer> categoryCounts = input
      .parallelStream()
      .map(g -> {
        Optional<String> optional = dataStore.getGraphString(g.getId(), SubgraphMiningPropertyKeys.CATEGORY);

        int category;
        if (optional.isPresent()) {
          category = dictionary.translate(optional.get());
          dataStore.setGraph(g.getId(), SubgraphMiningPropertyKeys.CATEGORY, category);
        } else {
          category = defaultCategory;
        }

        return category;
      })
      .collect(Collectors.groupingByConcurrent(Function.identity(), Collectors.counting()))
      .entrySet()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey, e -> Math.toIntExact(e.getValue())));


    Map<Integer, Integer> categoryMinSupports = categoryCounts
      .entrySet()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey, e -> Math.round(minSupport * e.getValue())));


    return new CharacteristicFactory(
      interestingness, categoryCounts, categoryMinSupports, input.size(), defaultCategory);
  }

  @Override
  protected GraphCollection pruneByLabels(GraphCollection inputCollection, GraphCollectionBuilder collectionBuilder) {
    // TODO preprocessing based on category frequencies
    return inputCollection;
  }
}
