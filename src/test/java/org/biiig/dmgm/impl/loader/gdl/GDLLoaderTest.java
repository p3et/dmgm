package org.biiig.dmgm.impl.loader.gdl;

import org.biiig.dmgm.impl.loader.GDLLoader;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GDLLoaderTest {

  @Test
  public void testSimpleGraph() {
    String gdl = "g1:G[(v1:A)-[:a]->(v1)-[:b]->(:B)], g2[(v1:A)-[:a]->(v1)-[:c]->(:C)]";

    GraphCollectionLoader loader = GDLLoader.fromString(gdl);

    GraphCollection graphCollection = loader.getGraphCollection();

    assertEquals("graph count", 2, graphCollection.size());
    assertEquals("label count", 8, graphCollection.getLabelDictionary().size());
    assertEquals("graph label", "G", graphCollection.getLabelDictionary().translate(graphCollection.getGraph(0).getLabel()));
  }

}