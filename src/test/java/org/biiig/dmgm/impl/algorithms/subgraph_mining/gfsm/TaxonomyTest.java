package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class TaxonomyTest {

  @Test
  public void addParentChild() throws InvalidArgumentException {
    StringTaxonomy taxonomy = new StringTaxonomy();
    taxonomy.add(StringTaxonomy.ROOT, "A");
    taxonomy.add("A", "AA");
    taxonomy.add("A", "AB");
    taxonomy.add(StringTaxonomy.ROOT, "B");

    assertEquals("parent", "A", taxonomy.getParent("AA"));
    assertTrue("children",
      Arrays.deepEquals(
        new String[] {"AA", "AB"},
        taxonomy.getChildren("A").toArray(new String[taxonomy.getChildren("A").size()])
      ));
  }

  @Test(expected = InvalidArgumentException.class)
  public void testSeparatorContainment() throws InvalidArgumentException {
    StringTaxonomy taxonomy = new StringTaxonomy();
    taxonomy.add(StringTaxonomy.ROOT, ".");
  }

  @Test(expected = InvalidArgumentException.class)
  public void testRootParent() throws InvalidArgumentException {
    StringTaxonomy taxonomy = new StringTaxonomy();
    taxonomy.add("X", StringTaxonomy.ROOT);
  }

  @Test(expected = InvalidArgumentException.class)
  public void testParentNotExisting() throws InvalidArgumentException {
    StringTaxonomy taxonomy = new StringTaxonomy();
    taxonomy.add("X", "Y");
  }

  @Test(expected = InvalidArgumentException.class)
  public void testChildAlreadyExisting() throws InvalidArgumentException {
    StringTaxonomy taxonomy = new StringTaxonomy();
    taxonomy.add(StringTaxonomy.ROOT, "Y");
    taxonomy.add(StringTaxonomy.ROOT, "Y");
  }
}