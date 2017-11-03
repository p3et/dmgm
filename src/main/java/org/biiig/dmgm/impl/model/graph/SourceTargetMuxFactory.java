package org.biiig.dmgm.impl.model.graph;

import org.biiig.dmgm.api.model.graph.DMGraph;

/**
 * Created by peet on 02.08.17.
 */
public class SourceTargetMuxFactory extends DirectedGraphFactoryBase {

  @Override
  public DMGraph create(int vertexCount, int edgeCount) {
    return new SourceTargetMux(vertexCount, edgeCount);
  }

}
