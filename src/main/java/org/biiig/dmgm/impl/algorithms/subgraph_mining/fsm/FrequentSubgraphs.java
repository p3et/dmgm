package org.biiig.dmgm.impl.algorithms.subgraph_mining.fsm;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningBase;

/**
 * Directed Multigraph gSpan
 */
public class FrequentSubgraphs extends SubgraphMiningBase implements WithFrequent {

  public FrequentSubgraphs(float minSupportRel, int maxEdgeCount) {
    super(minSupportRel, maxEdgeCount);
  }

  @Override
  protected FilterOrOutput<DFSCodeEmbeddingsPair> getFilterAndOutput(GraphCollection rawInput) {
    return getFrequent(rawInput, minSupport);
  }



}
