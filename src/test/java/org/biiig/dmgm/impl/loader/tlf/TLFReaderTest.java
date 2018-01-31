package org.biiig.dmgm.impl.loader.tlf;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.impl.loader.TLFConstants;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class TLFReaderTest extends DMGMTestBase {

  @Test
  public void testRead() throws IOException {
    GraphDB database = getPredictableDatabase();

    int graphLabel = database.encode(TLFConstants.GRAPH_SYMBOL);
    int colLabel = database.encode("COL");

    long[] graphIds = database.getElementsByLabel(i -> i == graphLabel);
    long cid = database.createCollection(colLabel, graphIds);


    List<CachedGraph> graphCollection = database.getCachedCollection(cid);

    assertEquals("graph count", 10, graphCollection.size());

    System.out.println(database);
  }

}