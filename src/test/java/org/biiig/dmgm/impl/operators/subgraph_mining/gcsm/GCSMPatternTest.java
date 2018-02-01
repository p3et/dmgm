package org.biiig.dmgm.impl.operators.subgraph_mining.gcsm;

import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningPatternTest;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedCharacteristicSubgraphs;

import java.util.function.Function;

public class GCSMPatternTest extends SubgraphMiningPatternTest {


  @Override
  protected Function<GraphDB, CollectionOperator> getOperator() {
    return db -> new GeneralizedCharacteristicSubgraphs(db,0.6f, 10);
  }

}
