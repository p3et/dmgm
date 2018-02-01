package org.biiig.dmgm.impl.operators.subgraph_mining;

import org.biiig.dmgm.api.GraphDB;

public class GeneralizedCharacteristicSubgraphs extends CharacteristicSubgraphs {


  public GeneralizedCharacteristicSubgraphs(GraphDB database, float minSupport, int maxEdgeCount) {
    super(database, minSupport, maxEdgeCount);
  }
}
