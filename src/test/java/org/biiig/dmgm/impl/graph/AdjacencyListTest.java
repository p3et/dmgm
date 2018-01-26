package org.biiig.dmgm.impl.graph;

public class AdjacencyListTest extends SingleLabelDirectedSmallGraphTest {

  @Override
  GraphFactory getFactory() {
    return new AdjacencyListFactory();
  }
}