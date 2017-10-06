package org.biiig.dmgm.model;

import org.biiig.dmgm.api.model.graph.DMGraphFactory;
import org.biiig.dmgm.impl.model.graph.DFSCodeFactory;

public class DFSCodeTest extends SingleLabelDirectedGraphTest {

  @Override
  DMGraphFactory getFactory() {
    return new DFSCodeFactory();
  }
}