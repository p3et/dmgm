package org.biiig.dmgm.impl.operators.subgraph_mining;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.Preprocessor;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.frequent.Frequent;

/**
 * Directed Multigraph gSpan
 */
public class FrequentSubgraphs extends SubgraphMiningBase implements Frequent {

  public FrequentSubgraphs(float minSupportRel, int maxEdgeCount) {
    super(minSupportRel, maxEdgeCount);
  }

  @Override
  protected Preprocessor getPreprocessor() {
    return getFrequentLabels(minSupport);
  }

  @Override
  protected FilterOrOutput<DFSCodeEmbeddingsPair> getFilterAndOutput(GraphCollection rawInput) {
    return getFilterOrOutput(rawInput, minSupport);
  }
}
