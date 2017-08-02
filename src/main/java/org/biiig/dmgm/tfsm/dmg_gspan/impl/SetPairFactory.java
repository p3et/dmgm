package org.biiig.dmgm.tfsm.dmg_gspan.impl;

import org.biiig.dmgm.tfsm.dmg_gspan.api.model.DirectedGraph;

/**
 * Created by peet on 02.08.17.
 */
public class SetPairFactory extends DirectedGraphFactoryBase {

  @Override
  public DirectedGraph create(int vertexCount, int edgeCount) {
    return new SetPair(vertexCount, edgeCount);
  }

}
