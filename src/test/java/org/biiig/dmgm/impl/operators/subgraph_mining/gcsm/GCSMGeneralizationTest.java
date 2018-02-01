package org.biiig.dmgm.impl.operators.subgraph_mining.gcsm;

import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedCharacteristicSubgraphs;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.GeneralizationTestBase;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class GCSMGeneralizationTest extends GeneralizationTestBase {

  @Override
  public Function<GraphDB, CollectionOperator> getOperator() {
    return db -> new GeneralizedCharacteristicSubgraphs(db,1.0f, 10);
  }
}
