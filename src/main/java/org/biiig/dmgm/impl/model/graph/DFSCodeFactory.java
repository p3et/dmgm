package org.biiig.dmgm.impl.model.graph;

import org.biiig.dmgm.api.model.graph.IntGraph;

public class DFSCodeFactory extends DirectedGraphFactoryBase {

  @Override
  public IntGraph create(int vertexCount, int edgeCount) {
    return new DFSCode(vertexCount, edgeCount);
  }
}
