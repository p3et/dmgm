package org.biiig.dmgm.impl.loader.gdl;

import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.impl.loader.GDLLoader;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class GDLLoaderTest {

  @Test
  public void testSimpleGraph() {
    String gdl = "g1:G[(v1:A)-[:a]->(v1)-[:b]->(:B)], g2:G[(v1:A)-[:a]->(v1)-[:c]->(:C)]";

    GraphDB db = GDLLoader.fromString(gdl).get();

    int graphLabel = db.encode("G");
    int colLabel = db.encode("COL");

    long[] graphIds = db.getElementsByLabel(i -> i == graphLabel);
    Long colId = db.createCollection(colLabel, graphIds);
    List<CachedGraph> graphCollection = db.getCachedCollection(colId);

    assertEquals("graph count", 2, graphCollection.size());

    System.out.println(db);
  }

}