package org.biiig.dmgm.impl.operators.subgraph_mining;

import org.biiig.dmgm.api.GraphDB;

public class GeneralizedFrequentSubgraphs extends FrequentSubgraphs {

  public GeneralizedFrequentSubgraphs(GraphDB database, float minSupport, int maxEdgeCount) {
    super(database, minSupport, maxEdgeCount);
  }
}
