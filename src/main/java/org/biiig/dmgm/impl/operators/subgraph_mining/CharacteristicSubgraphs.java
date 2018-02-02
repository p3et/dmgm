package org.biiig.dmgm.impl.operators.subgraph_mining;

import org.biiig.dmgm.api.GraphDB;

/**
 * min frequency is with regard to f
 */
public class CharacteristicSubgraphs extends GeneralizedCharacteristicSubgraphs {

  public CharacteristicSubgraphs(GraphDB database, float minSupportRel, int maxEdgeCount) {
    super(database, minSupportRel, maxEdgeCount);
  }
}
