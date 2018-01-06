package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import com.google.common.collect.Maps;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.graph_loader.gdl.GDLLoader;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GFSMAlgorithmTest extends DMGMTestBase {

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

    Operator operator = new GeneralizedFrequentSubgraphs(0.0f, 10, taxonomies);

    String inputGDL =
      ":X[(:A{_taxonomyValue:\"aa\"})-[:a]->(:B{_taxonomyValue:\"bb\"})-[:a]->(:C)]" +
      ":X[(:A{_taxonomyValue:\"ab\"})-[:a]->(:B{_taxonomyValue:\"bbb\"})-[:a]->(:C)]";

    GraphCollection input = GDLLoader
      .fromString(inputGDL)
      .getGraphCollection();

    GraphCollection output = input
      .apply(operator);

    assertEquals(34, output.size());
  }
}
