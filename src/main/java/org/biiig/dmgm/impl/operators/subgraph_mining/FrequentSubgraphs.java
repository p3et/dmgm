package org.biiig.dmgm.impl.operators.subgraph_mining;

import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.frequent.Frequent;

/**
 * Directed Multigraph gSpan
 */
public class FrequentSubgraphs extends SubgraphMiningBase implements Frequent {

  public FrequentSubgraphs(float minSupportRel, int maxEdgeCount) {
    super(minSupportRel, maxEdgeCount);
  }

}
