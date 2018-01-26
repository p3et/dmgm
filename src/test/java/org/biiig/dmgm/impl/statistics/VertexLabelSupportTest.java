package org.biiig.dmgm.impl.statistics;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.HyperVertexDB;
import org.biiig.dmgm.impl.loader.TLFConstants;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class VertexLabelSupportTest extends DMGMTestBase {

  private final HyperVertexDB db;
  private final long cid;

  public VertexLabelSupportTest() throws IOException {
    db = getPredictableDatabase();
    int label = db.encode(TLFConstants.GRAPH_SYMBOL);
    this.cid = db.createCollectionByLabel(label, label);
  }

  @Test
  public void getAbsolute() throws IOException {
    Map<Integer, Integer> support = new VertexLabelSupport()
      .getAbsolute(db, cid);

    for (int i = 1; i <= 10; i++)
      assertTrue("cannot find support of 1", support.values().contains(i));
  }

  @Test
  public void getRelative() throws IOException {
    Map<Integer, Double> support = new VertexLabelSupport()
      .getRelative(db, cid);

    for (int i = 1; i <= 10; i++)
      assertTrue("cannot find support of 1", support.values().contains((double) i / 10));
  }

//  @Test
//  public void generalization() throws IOException {
//    SmallGraph graph = new SmallGraphBase(id, label, vertexLabels, edgeLabels, sourceIds, targetIds);
//
//    String label = "A_a_a_a";
//    graph.addVertex(collection.getLabelDictionary().translate(label));
//
//    collection.add(graph);
//
//    Map<Integer, Integer> support = new VertexLabelSupport().getAbsolute(collection);
//
//    assertEquals("specializations are missing", 3, support.size());
//  }
}