package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.fsm.WithFrequent;

public class GeneralizedFrequentSubgraphs extends GeneralizedSubgraphsBase implements WithFrequent {

  public GeneralizedFrequentSubgraphs(float minSupportRel, int maxEdgeCount) {
    super(minSupportRel, maxEdgeCount);
  }

  @Override
  protected FilterOrOutput<DFSCodeEmbeddingsPair> getFilterAndOutput(GraphCollection rawInput) {
    FilterOrOutput<PatternVectorsPair> vectorFilter = getFrequent(rawInput, minSupport);
    FilterOrOutput<DFSCodeEmbeddingsPair> patternFilter = getFrequent(rawInput, minSupport);

    Specializer spezializer = getSpecializer(rawInput, vectorFilter);

    return new Generalized(patternFilter, spezializer);
  }

}
