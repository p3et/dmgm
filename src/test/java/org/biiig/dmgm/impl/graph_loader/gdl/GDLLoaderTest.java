package org.biiig.dmgm.impl.graph_loader.gdl;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.GraphCollectionLoader;
import org.junit.Test;

import java.io.IOException;

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