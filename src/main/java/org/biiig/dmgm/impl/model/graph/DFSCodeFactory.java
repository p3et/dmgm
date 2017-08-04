package org.biiig.dmgm.impl.model.graph;

import org.biiig.dmgm.api.model.graph.DirectedGraph;

public class DFSCodeFactory extends DirectedGraphFactoryBase {

  @Override
  public DirectedGraph create(int vertexCount, int edgeCount) {
    return new DFSCode(vertexCount, edgeCount);
  }
}
