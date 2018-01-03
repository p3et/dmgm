package org.biiig.dmgm.impl.algorithms.subgraph_mining.ccp;

import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutputFactory;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningBase;

import java.util.Map;
import java.util.stream.Collectors;

public class CategoryCharacteristicSubgraphs extends SubgraphMiningBase {

  public static final String CATEGORY_KEY = "_category";

  private final Categorization categorization;
  private final Interestingness interestingness;

  /**
   * @param categorization
   * @param interestingness */

  public CategoryCharacteristicSubgraphs(
    float minSupportRel, int maxEdgeCount, Categorization categorization, Interestingness interestingness) {

    super(minSupportRel, maxEdgeCount);

    this.categorization = categorization;
    this.interestingness = interestingness;
  }

  @Override
  public FilterAndOutputFactory getFilterAndOutputFactory(GraphCollection rawInput) {
    Map<Integer, String> categorizedGraphs = rawInput
      .parallelStream()
      .collect(Collectors.toConcurrentMap(Graph::getId, categorization::categorize));

    return new CategoryCharacteristicFactory(categorizedGraphs, interestingness);
  }

}
