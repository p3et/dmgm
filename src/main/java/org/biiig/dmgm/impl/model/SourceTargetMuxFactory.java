package org.biiig.dmgm.impl.model;

import org.biiig.dmgm.model.DirectedGraph;

/**
 * Created by peet on 02.08.17.
 */
public class SourceTargetMuxFactory extends DirectedGraphFactoryBase {

  @Override
  public DirectedGraph create(int vertexCount, int edgeCount) {
    return new SourceTargetMux(vertexCount, edgeCount);
  }

}
