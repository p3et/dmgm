package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.junit.Test;

public class TaxonomyTest {

  @Test
  public void add() throws InvalidArgumentException {
    StringTaxonomy taxonomy = new StringTaxonomy();
    taxonomy.add(StringTaxonomy.ROOT, "A");
    taxonomy.add("A", "AA");
    taxonomy.add(StringTaxonomy.ROOT, "B");

    System.out.println(taxonomy.getParent("AA"));

    System.out.println(taxonomy);
  }
}