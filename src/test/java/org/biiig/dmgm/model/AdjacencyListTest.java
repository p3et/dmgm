package org.biiig.dmgm.model;

import org.biiig.dmgm.api.model.graph.DMGraphFactory;
import org.biiig.dmgm.impl.model.graph.AdjacencyListFactory;

public class AdjacencyListTest extends SingleLabelDirectedGraphTest {

  @Override
  DMGraphFactory getFactory() {
    return new AdjacencyListFactory();
  }
}