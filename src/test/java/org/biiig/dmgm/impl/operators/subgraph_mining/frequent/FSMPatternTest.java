package org.biiig.dmgm.impl.operators.subgraph_mining.frequent;

import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.db.GraphDBBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.FrequentSubgraphs;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningPatternTest;

import java.util.function.Function;

public class FSMPatternTest extends SubgraphMiningPatternTest {


  @Override
  protected Function<GraphDB, CollectionOperator> getOperator() {
    return db -> new FrequentSubgraphs(db,0.6f, 10);
  }

}
