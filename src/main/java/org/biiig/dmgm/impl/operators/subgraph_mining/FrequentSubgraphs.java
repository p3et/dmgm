package org.biiig.dmgm.impl.operators.subgraph_mining;

import org.biiig.dmgm.api.GraphDB;

/**
 * Directed Multigraph gSpan
 */
public class FrequentSubgraphs extends GeneralizedCharacteristicSubgraphs {
  public FrequentSubgraphs(GraphDB database, float minSupportRel, int maxEdgeCount) {
    super(database, minSupportRel, maxEdgeCount);
  }
}
