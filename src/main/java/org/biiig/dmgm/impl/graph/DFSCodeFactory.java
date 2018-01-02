package org.biiig.dmgm.impl.graph;

import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphFactory;

public class DFSCodeFactory implements GraphFactory {

  @Override
  public Graph create() {
    return new DFSCode();
  }
}
