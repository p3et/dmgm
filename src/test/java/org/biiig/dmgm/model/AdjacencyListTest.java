package org.biiig.dmgm.model;

import org.biiig.dmgm.api.model.graph.IntGraphFactory;
import org.biiig.dmgm.impl.model.graph.AdjacencyListFactory;

public class AdjacencyListTest extends SingleLabelDirectedGraphTest {

  @Override
  IntGraphFactory getFactory() {
    return new AdjacencyListFactory();
  }
}