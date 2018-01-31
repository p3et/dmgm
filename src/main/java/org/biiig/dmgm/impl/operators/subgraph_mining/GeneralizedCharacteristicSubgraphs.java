package org.biiig.dmgm.impl.operators.subgraph_mining;

import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.operators.subgraph_mining.characteristic.Interestingness;
import org.biiig.dmgm.impl.operators.subgraph_mining.characteristic.Characteristic;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.Preprocessor;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.PropertyKeys;
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


}
