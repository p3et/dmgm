package org.biiig.dmgm.impl.loader.gdl;

import org.biiig.dmgm.api.HyperVertexDB;
import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.impl.loader.GDLLoader;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class GDLLoaderTest {

  @Test
  public void testSimpleGraph() {
    String gdl = "g1:G[(v1:A)-[:a]->(v1)-[:b]->(:B)], g2:G[(v1:A)-[:a]->(v1)-[:c]->(:C)]";

    HyperVertexDB db = GDLLoader.fromString(gdl).get();

    int graphLabel = db.encode("G");
    int colLabel = db.encode("COL");

    Long colId = db.createCollectionByLabel(graphLabel, colLabel);
    List<SmallGraph> graphCollection = db.getCollection(colId);

    assertEquals("graph count", 2, graphCollection.size());

    System.out.println(db);
  }

}