package org.biiig.dmgm.impl.algorithms.subgraph_mining.csm;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.GraphCollectionBuilder;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningBase;

/**
 * min frequency is with regard to f
 */
public class CharacteristicSubgraphs extends SubgraphMiningBase implements WithCharacteristic {

  private final Interestingness interestingness;

  /**
   * @param interestingness
   * */

  public CharacteristicSubgraphs(
    float minSupportRel, int maxEdgeCount, Interestingness interestingness) {

    super(minSupportRel, maxEdgeCount);
    this.interestingness = interestingness;
  }

  @Override
  protected FilterOrOutput<DFSCodeEmbeddingsPair> getFilterAndOutput(GraphCollection rawInput) {
    return getCharacteristic(rawInput, minSupport, interestingness);
  }


  @Override
  protected GraphCollection pruneByLabels(GraphCollection inputCollection, GraphCollectionBuilder collectionBuilder) {
    // TODO preprocessing based on category frequencies
    return inputCollection;
  }
}
