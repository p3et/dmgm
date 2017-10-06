package org.biiig.dmgm.impl.model.graph;

import org.biiig.dmgm.api.model.graph.DMGraph;

public class DFSCodeFactory extends DirectedGraphFactoryBase {

  @Override
  public DMGraph create(int vertexCount, int edgeCount) {
    return new DFSCode(vertexCount, edgeCount);
  }
}
