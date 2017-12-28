package org.biiig.dmgm.impl.model.source.gdl;

import org.biiig.dmgm.api.model.collection.IntGraphCollection;
import org.biiig.dmgm.api.model.source.DMGraphDataSource;
import org.biiig.dmgm.impl.model.collection.InMemoryGraphCollection;
import org.biiig.dmgm.impl.model.graph.IntGraphBaseFactory;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GDLDataSourceTest {

  @Test
  public void testSimpleGraph() throws IOException {
    String gdl = "g1[(v1:A)-[:a]->(v1)-[:b]->(:B)], g2[(v1:A)-[:a]->(v1)-[:c]->(:C)]";

    IntGraphCollection database = new InMemoryGraphCollection();
    DMGraphDataSource dataSource = new GDLDataSource(gdl);
    dataSource.load(database, new IntGraphBaseFactory());

    assertEquals("graph count", 2, database.size());
  }

}