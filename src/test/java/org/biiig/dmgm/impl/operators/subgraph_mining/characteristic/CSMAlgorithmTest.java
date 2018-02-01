package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.operators.subgraph_mining.CharacteristicSubgraphs;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedCharacteristicSubgraphs;

import java.util.function.Function;

public class CSMAlgorithmTest extends CharacteristicTestBase {

  @Override
  public Function<GraphDB, CollectionOperator> getOperator() {
    return db -> new CharacteristicSubgraphs(
      db,1.0f,10);
  }
}
