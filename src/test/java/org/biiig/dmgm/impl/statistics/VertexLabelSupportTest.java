package org.biiig.dmgm.impl.statistics;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.graph.GraphBase;
import org.biiig.dmgm.impl.graph_collection.InMemoryGraphCollection;
import org.biiig.dmgm.impl.graph_collection.InMemoryGraphCollectionBuilderFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

public class VertexLabelSupportTest extends DMGMTestBase {

  @Test
  public void getAbsolute() throws IOException {
    Map<Integer, Integer> support = new VertexLabelSupport()
      .getAbsolute(getPredictableDatabase());

    for (int i = 1; i <= 10; i++)
      assertTrue("cannot find support of 1", support.values().contains(i));
  }

  @Test
  public void getRelative() throws IOException {
    Map<Integer, Double> support = new VertexLabelSupport()
      .getRelative(getPredictableDatabase());

    for (int i = 1; i <= 10; i++)
      assertTrue("cannot find support of 1", support.values().contains((double) i / 10));
  }

  @Test
  public void generalization() throws IOException {
    GraphCollection collection = new InMemoryGraphCollectionBuilderFactory().create().create();

    Graph graph = new GraphBase();

    String label = "A_a_a_a";
    graph.addVertex(collection.getLabelDictionary().translate(label));

    collection.add(graph);

    Map<Integer, Integer> support = new VertexLabelSupport().getAbsolute(collection);

    assertEquals("specializations are missing", 3, support.size());
  }
}