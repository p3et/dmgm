package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedCharacteristicSubgraphs;

public abstract class GeneralizedSubgraphsBase extends GeneralizedCharacteristicSubgraphs {
  public GeneralizedSubgraphsBase(GraphDB database, float minSupportRel, int maxEdgeCount) {
    super(database, minSupportRel, maxEdgeCount);
  }
}
