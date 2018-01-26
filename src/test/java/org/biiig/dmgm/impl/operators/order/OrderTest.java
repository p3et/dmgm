package org.biiig.dmgm.impl.operators.order;

import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.impl.loader.GDLLoader;
import org.junit.Test;

import java.util.Comparator;

public class OrderTest {

  @Test
  public void apply() {
    String gdl = ":G[(:A)],:G[]";

    GraphCollection input = GDLLoader.fromString(gdl).getGraphCollection();

    assertEquals(1, input.getGraph(0).getVertexCount());
    assertEquals(0, input.getGraph(1).getVertexCount());

    GraphCollection output = input.apply(Order.by(Comparator.comparingInt(SmallGraph::getVertexCount)));

    assertEquals(0, output.getGraph(0).getVertexCount());
    assertEquals(1, output.getGraph(1).getVertexCount());
  }
}