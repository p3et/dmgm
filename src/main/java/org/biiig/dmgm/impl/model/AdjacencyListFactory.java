package org.biiig.dmgm.impl.model;

import org.biiig.dmgm.api.model.DirectedGraph;

/**
 * Created by peet on 02.08.17.
 */
public class AdjacencyListFactory extends DirectedGraphFactoryBase {

  @Override
  public DirectedGraph create(int vertexCount, int edgeCount) {
    return new AdjacencyList(vertexCount, edgeCount);
  }
}
