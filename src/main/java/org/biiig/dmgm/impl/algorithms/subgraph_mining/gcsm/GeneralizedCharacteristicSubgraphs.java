package org.biiig.dmgm.impl.algorithms.subgraph_mining.gcsm;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.GraphCollectionBuilder;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.csm.Interestingness;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.csm.WithCharacteristic;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm.Generalized;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm.GeneralizedSubgraphsBase;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm.PatternVectorsPair;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm.Specializer;

public class GeneralizedCharacteristicSubgraphs extends GeneralizedSubgraphsBase implements WithCharacteristic {

  private final Interestingness interestingness;

  public GeneralizedCharacteristicSubgraphs(float minSupportRel, int maxEdgeCount, Interestingness interestingness) {
    super(minSupportRel, maxEdgeCount);
    this.interestingness = interestingness;
  }

  @Override
  protected FilterOrOutput<DFSCodeEmbeddingsPair> getFilterAndOutput(GraphCollection rawInput) {
    FilterOrOutput<PatternVectorsPair> vectorFilter = getCharacteristic(rawInput, minSupport, interestingness);
    FilterOrOutput<DFSCodeEmbeddingsPair> patternFilter = getCharacteristic(rawInput, minSupport, interestingness);

    Specializer spezializer = getSpecializer(rawInput, vectorFilter);

    return new Generalized(patternFilter, spezializer);
  }

  @Override
  protected GraphCollection pruneByLabels(GraphCollection inputCollection, GraphCollectionBuilder collectionBuilder) {
    // TODO preprocessing based on category frequencies
    return inputCollection;
  }

}
