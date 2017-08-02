package org.biiig.dmgm.impl.model;

import org.biiig.dmgm.api.model.DirectedGraph;

public class DFSCodeFactory extends DirectedGraphFactoryBase {

  @Override
  public DirectedGraph create(int vertexCount, int edgeCount) {
    return new DFSCode(vertexCount, edgeCount);
  }
}
