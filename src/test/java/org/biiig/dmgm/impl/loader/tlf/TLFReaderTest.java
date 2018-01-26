package org.biiig.dmgm.impl.loader.tlf;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.HyperVertexDB;
import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.impl.loader.TLFConstants;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class TLFReaderTest extends DMGMTestBase {

  @Test
  public void testRead() throws IOException {
    HyperVertexDB database = getPredictableDatabase();

    int graphLabel = database.encode(TLFConstants.GRAPH_SYMBOL);
    int colLabel = database.encode("COL");

    long cid = database.createCollectionByLabel(graphLabel, colLabel);


    List<SmallGraph> graphCollection = database.getCollection(cid);

    assertEquals("graph count", 10, graphCollection.size());

    System.out.println(database);
  }

}