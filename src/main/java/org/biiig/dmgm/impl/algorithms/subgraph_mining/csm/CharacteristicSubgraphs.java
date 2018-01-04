package org.biiig.dmgm.impl.algorithms.subgraph_mining.csm;

import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutputFactory;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningBase;

import java.util.Map;
import java.util.stream.Collectors;

public class CharacteristicSubgraphs extends SubgraphMiningBase {

  private final Categorization categorization;
  private final Interestingness interestingness;

  /**
   * @param categorization
   * @param interestingness */

  public CharacteristicSubgraphs(
    float minSupportRel, int maxEdgeCount, Categorization categorization, Interestingness interestingness) {

    super(minSupportRel, maxEdgeCount);

    this.categorization = categorization;
    this.interestingness = interestingness;
  }

  @Override
  public FilterAndOutputFactory getFilterAndOutputFactory(GraphCollection input) {
    Map<Integer, String> graphCategory = input
      .parallelStream()
      .collect(Collectors.toConcurrentMap(Graph::getId, categorization::categorize));

    return new CharacteristicFactory(graphCategory, interestingness);
  }

}
