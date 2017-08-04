package org.biiig.dmgm.model;

import org.biiig.dmgm.api.model.graph.DirectedGraphFactory;
import org.biiig.dmgm.impl.model.graph.AdjacencyListFactory;

public class AdjacencyListTest extends DirectedGraphTest {

  @Override
  DirectedGraphFactory getFactory() {
    return new AdjacencyListFactory();
  }
}