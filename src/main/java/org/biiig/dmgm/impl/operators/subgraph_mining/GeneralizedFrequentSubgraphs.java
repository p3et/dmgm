package org.biiig.dmgm.impl.operators.subgraph_mining;

import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.Preprocessor;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningPropertyKeys;
import org.biiig.dmgm.impl.operators.subgraph_mining.frequent.Frequent;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.Generalized;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.GeneralizedSubgraphsBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.PatternVectorsPair;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.Specializer;

import java.util.List;

public class GeneralizedFrequentSubgraphs extends GeneralizedSubgraphsBase implements Frequent {

  public GeneralizedFrequentSubgraphs(float minSupportRel, int maxEdgeCount) {
    super(minSupportRel, maxEdgeCount);
  }

  @Override
  public Preprocessor getPreprocessor() {
    return getFrequentLabels(minSupport);
  }

  @Override
  protected FilterOrOutput<DFSCodeEmbeddingsPair> getFilterAndOutput(List<CachedGraph> rawInput, GraphDB db) {
    FilterOrOutput<PatternVectorsPair> vectorFilter = getFilterOrOutput(rawInput, minSupport);
    FilterOrOutput<DFSCodeEmbeddingsPair> patternFilter = getFilterOrOutput(rawInput, minSupport);

    Specializer spezializer = getSpecializer(rawInput, vectorFilter, db, db.encode(SubgraphMiningPropertyKeys.TAXONOMY_PATH));

    return new Generalized(patternFilter, spezializer);
  }
}
