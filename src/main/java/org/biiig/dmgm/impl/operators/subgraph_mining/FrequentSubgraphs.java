package org.biiig.dmgm.impl.operators.subgraph_mining;

import org.biiig.dmgm.api.HyperVertexDB;
import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.Preprocessor;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.frequent.Frequent;

import java.util.List;

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
  protected FilterOrOutput<DFSCodeEmbeddingsPair> getFilterAndOutput(List<SmallGraph> rawInput, HyperVertexDB db) {
    return getFilterOrOutput(rawInput, minSupport);
  }
}
