package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedFrequentSubgraphs;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningPatternTest;

import java.util.function.Function;

public class GFSMPatternTest extends SubgraphMiningPatternTest {


  @Override
  protected Function<GraphDB, CollectionOperator> getOperator() {
    return db -> new GeneralizedFrequentSubgraphs(db, 0.6f, 10);
  }

}
