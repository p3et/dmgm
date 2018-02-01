package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.db.GraphDBBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.CharacteristicSubgraphs;
import org.junit.Test;

import java.util.function.Function;

public class CSMAlgorithmTest extends DMGMTestBase {

  @Test
  public void testAlgorithm() {
    Function<GraphDB, CollectionOperator> operator =
      db -> new CharacteristicSubgraphs(db,      1.0f, 10);

    String gdl =
      ":IN{_category:\"X\"}[(:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":IN{_category:\"X\"}[(:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":IN{_category:\"Y\"}[(:A)-[:a]->(:B)-[:b]->(:C)-[:b]->(:D)]" +
      ":IN{_category:\"Y\"}[(:A)-[:a]->(:B)-[:b]->(:C)-[:b]->(:E)]" +
      ":EX{_category:\"X\"}[(:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":EX{_category:\"X\"}[(:A)-[:a]->(:B)-[:a]->(:C)]" +
      ":EX{_category:\"X\"}[(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":EX{_category:\"X\"}[(:B)-[:a]->(:C)]" +
      ":EX{_category:\"X\"}[(:C)-[:a]->(:D)]" +
      ":EX{_category:\"X\",_support:4,_frequency:4,_dfsCode:\"0:A-a->1:B\"}[(:A)-[:a]->(:B)]" +
      ":EX{_category:\"X\"}[(:A)-[:a]->(:B)-[:b]->(:C)]" +
      ":EX{_category:\"X\"}[(:B)-[:b]->(:C)]" ;

    runAndTestExpectation(operator, gdl, false);
  }
}
