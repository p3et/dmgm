package org.biiig.dmgm.impl.model.source.gdl;

import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.api.model.source.DMGraphDataSource;
import org.biiig.dmgm.impl.model.collection.InMemoryGraphCollection;
import org.biiig.dmgm.impl.model.graph.SourceTargetMuxFactory;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GDLDataSourceTest {

  @Test
  public void testSimpleGraph() throws IOException {
    String gdl = "g1[(v1:A)-[:a]->(v1)-[:b]->(:B)], g2[(v1:A)-[:a]->(v1)-[:c]->(:C)]";

    DMGraphCollection database = new InMemoryGraphCollection();
    DMGraphDataSource dataSource = new GDLDataSource(gdl);
    dataSource.load(database, new SourceTargetMuxFactory());

    assertEquals("graph count", 2, database.getGraphCount());
  }

}