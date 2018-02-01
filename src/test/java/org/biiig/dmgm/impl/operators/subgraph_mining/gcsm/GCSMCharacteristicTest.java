package org.biiig.dmgm.impl.operators.subgraph_mining.gcsm;

import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedCharacteristicSubgraphs;

import java.util.function.Function;

public class GCSMCharacteristicTest extends org.biiig.dmgm.impl.operators.subgraph_mining.characteristic.CharacteristicTestBase {

  @Override
  public Function<GraphDB, CollectionOperator> getOperator() {
    return db -> new GeneralizedCharacteristicSubgraphs(
    db,1.0f,10);
  }
}
