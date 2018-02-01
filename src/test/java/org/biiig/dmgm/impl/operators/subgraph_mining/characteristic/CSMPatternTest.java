package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.operators.subgraph_mining.CharacteristicSubgraphs;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningPatternTest;

import java.util.function.Function;

public class CSMPatternTest extends SubgraphMiningPatternTest {


  @Override
  protected Function<GraphDB, CollectionOperator> getOperator() {
    return db -> new CharacteristicSubgraphs(db, 0.6f, 10);
  }

}
