package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedFrequentSubgraphs;

import java.util.function.Function;

public class GFSMGeneralizationTest extends GeneralizationTestBase {

  @Override
  public Function<GraphDB, CollectionOperator> getOperator() {
    return db -> new GeneralizedFrequentSubgraphs(db,1.0f, 10);
  }
}
