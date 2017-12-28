package org.biiig.dmgm.impl.model.graph;

import org.biiig.dmgm.api.model.graph.IntGraph;
import org.biiig.dmgm.api.model.graph.IntGraphFactory;

public class IntGraphBaseFactory implements IntGraphFactory {
  @Override
  public IntGraph create() {
    return new IntGraphBase();
  }
}
