package org.biiig.dmgm.impl.model.graph;

import org.biiig.dmgm.api.model.graph.IntGraph;

/**
 * Created by peet on 02.08.17.
 */
public class AdjacencyListFactory extends DirectedGraphFactoryBase {

  @Override
  public IntGraph create(int vertexCount, int edgeCount) {
    return new AdjacencyList(vertexCount, edgeCount);
  }
}
