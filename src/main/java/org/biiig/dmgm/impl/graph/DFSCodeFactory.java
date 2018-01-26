package org.biiig.dmgm.impl.graph;

import org.biiig.dmgm.api.SmallGraph;

public class DFSCodeFactory implements GraphFactory {

  @Override
  public SmallGraph create() {
    return new DFSCode();
  }
}
