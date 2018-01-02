package org.biiig.dmgm.impl.model.source.gdl;

import org.biiig.dmgm.api.model.collection.GraphCollection;
import org.biiig.dmgm.api.model.source.GraphCollectionLoader;
import org.biiig.dmgm.impl.model.collection.InMemoryGraphCollection;
import org.biiig.dmgm.impl.model.graph.IntGraphBaseFactory;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GDLLoaderTest {

  @Test
  public void testSimpleGraph() throws IOException {
    String gdl = "g1[(v1:A)-[:a]->(v1)-[:b]->(:B)], g2[(v1:A)-[:a]->(v1)-[:c]->(:C)]";

    GraphCollectionLoader loader = GDLLoader.fromString(gdl);

    assertEquals("graph count", 2, loader.getGraphCollection().size());
  }

}