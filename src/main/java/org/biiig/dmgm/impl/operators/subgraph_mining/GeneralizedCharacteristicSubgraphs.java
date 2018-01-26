package org.biiig.dmgm.impl.operators.subgraph_mining;

import org.biiig.dmgm.api.HyperVertexDB;
import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.operators.subgraph_mining.characteristic.Interestingness;
import org.biiig.dmgm.impl.operators.subgraph_mining.characteristic.Characteristic;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.Preprocessor;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningPropertyKeys;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.Generalized;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.GeneralizedSubgraphsBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.PatternVectorsPair;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.Specializer;

import java.util.List;

public class GeneralizedCharacteristicSubgraphs extends GeneralizedSubgraphsBase implements Characteristic {

  private final Interestingness interestingness;

  public GeneralizedCharacteristicSubgraphs(float minSupportRel, int maxEdgeCount, Interestingness interestingness) {
    super(minSupportRel, maxEdgeCount);
    this.interestingness = interestingness;
  }

  @Override
  protected Preprocessor getPreprocessor() {
    return getCharacteristicLabels(minSupport);
  }

  @Override
  protected FilterOrOutput<DFSCodeEmbeddingsPair> getFilterAndOutput(List<SmallGraph> rawInput, HyperVertexDB db) {
    FilterOrOutput<PatternVectorsPair> vectorFilter = getCharacteristicFilter(rawInput, minSupport, interestingness);
    FilterOrOutput<DFSCodeEmbeddingsPair> patternFilter = getCharacteristicFilter(rawInput, minSupport, interestingness);

    Specializer spezializer = getSpecializer(rawInput, vectorFilter, db, db.encode(SubgraphMiningPropertyKeys.TAXONOMY_PATH));

    return new Generalized(patternFilter, spezializer);
  }

}
