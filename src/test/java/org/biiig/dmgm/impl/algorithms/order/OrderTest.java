package org.biiig.dmgm.impl.algorithms.order;

import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.graph_loader.gdl.GDLLoader;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class OrderTest {

  @Test
  public void apply() {
    String gdl = ":G[(:A)],:G[]";

    GraphCollection input = GDLLoader.fromString(gdl).getGraphCollection();

    assertEquals(1, input.getGraph(0).getVertexCount());
    assertEquals(0, input.getGraph(1).getVertexCount());

    GraphCollection output = input.apply(Order.by(Comparator.comparingInt(Graph::getVertexCount)));

    assertEquals(0, output.getGraph(0).getVertexCount());
    assertEquals(1, output.getGraph(1).getVertexCount());
  }
}