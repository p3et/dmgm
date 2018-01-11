package org.biiig.dmgm.impl.algorithms.subgraph_mining.gcsm;

import com.google.common.collect.Maps;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.gcsm.GeneralizedCharacteristicSubgraphs;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm.GeneralizedFrequentSubgraphs;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm.StringTaxonomy;
import org.biiig.dmgm.impl.graph_loader.gdl.GDLLoader;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GFSMGeneralizationTest extends DMGMTestBase {

  @Test
  public void testAlgorithm() throws InvalidArgumentException {
    // balanced
    StringTaxonomy aTaxonomy = new StringTaxonomy();
    aTaxonomy.add(StringTaxonomy.ROOT, "a");
    aTaxonomy.add("a", "aa");
    aTaxonomy.add("a", "ab");

    // unbalanced
    StringTaxonomy bTaxonomy = new StringTaxonomy();
    bTaxonomy.add(StringTaxonomy.ROOT, "b");
    bTaxonomy.add("b", "bb");
    bTaxonomy.add("bb", "bbb");

    Map<String, StringTaxonomy> taxonomies = Maps.newHashMap();
    taxonomies.put("A", aTaxonomy);
    taxonomies.put("B", bTaxonomy);

    Operator operator = new GeneralizedCharacteristicSubgraphs(1.0f, 10, (f, t) -> new int[] {0});

    String inputGDL = (
      ":X[(:A.a.a)-[:a]->(:B.b.b)-[:a]->(:C)]" +
      ":X[(:A.a.b)-[:a]->(:B.b.b.b)-[:a]->(:C)]" )
      .replace(".", GeneralizedFrequentSubgraphs.LEVEL_SEPARATOR);

    String expectedGDL = (
      ":X[(:A.a)-[:a]->(:B.b)-[:a]->(:C)]" +
      ":X[(:A.a)-[:a]->(:B.b.b)-[:a]->(:C)]" +
      ":X[(:A.a)-[:a]->(:B.b)]" +
      ":X[(:A.a)-[:a]->(:B.b.b)]" +
      ":X[(:B.b)-[:a]->(:C)]" +
      ":X[(:B.b.b)-[:a]->(:C)]"
    )
      .replace(".", GeneralizedFrequentSubgraphs.LEVEL_SEPARATOR);

    runAndTestExpectation(operator, inputGDL, expectedGDL);
  }
}
