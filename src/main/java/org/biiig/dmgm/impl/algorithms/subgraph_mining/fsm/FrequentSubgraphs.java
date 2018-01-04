package org.biiig.dmgm.impl.algorithms.subgraph_mining.fsm;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutputFactory;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningBase;

/**
 * Directed Multigraph gSpan
 */
public class FrequentSubgraphs extends SubgraphMiningBase {

  public FrequentSubgraphs(float minSupportRel, int maxEdgeCount) {
    super(minSupportRel, maxEdgeCount);
  }

  @Override
  public FilterAndOutputFactory getFilterAndOutputFactory(GraphCollection input) {
    return new FrequentFactory();
  }
}
