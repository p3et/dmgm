package org.biiig.dmgm.impl.operators.subgraph_mining;

import org.biiig.dmgm.api.HyperVertexDB;
import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.Preprocessor;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.characteristic.Interestingness;
import org.biiig.dmgm.impl.operators.subgraph_mining.characteristic.Characteristic;

import java.util.List;

/**
 * min frequency is with regard to f
 */
public class CharacteristicSubgraphs extends SubgraphMiningBase implements Characteristic {

  private final Interestingness interestingness;

  /**
   * @param interestingness
   * */

  public CharacteristicSubgraphs(
    float minSupportRel, int maxEdgeCount, Interestingness interestingness) {

    super(minSupportRel, maxEdgeCount);
    this.interestingness = interestingness;
  }

  public Preprocessor getPreprocessor() {
    return getCharacteristicLabels(minSupport);
  }

  @Override
  protected FilterOrOutput<DFSCodeEmbeddingsPair> getFilterAndOutput(List<SmallGraph> rawInput, HyperVertexDB db) {
    return getCharacteristicFilter(rawInput, minSupport, interestingness);
  }
}
