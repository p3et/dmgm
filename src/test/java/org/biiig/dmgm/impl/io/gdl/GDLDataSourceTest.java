package org.biiig.dmgm.impl.io.gdl;

import org.biiig.dmgm.api.DMGraphDatabase;
import org.biiig.dmgm.api.io.DMGraphDataSource;
import org.biiig.dmgm.impl.InMemoryDatabase;
import org.biiig.dmgm.impl.model.graph.SourceTargetMuxFactory;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GDLDataSourceTest {

  @Test
  public void testSimpleGraph() throws IOException {
    String gdl = "g1[(v1:A)-[:a]->(v1)-[:b]->(:B)], g2[(v1:A)-[:a]->(v1)-[:c]->(:C)]";

    DMGraphDatabase database = new InMemoryDatabase();
    DMGraphDataSource dataSource = new GDLDataSource(gdl);
    dataSource.load(database, new SourceTargetMuxFactory());

    assertEquals("graph count", 2, database.getGraphCount());
  }

}