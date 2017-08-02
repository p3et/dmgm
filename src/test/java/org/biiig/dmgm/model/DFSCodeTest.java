package org.biiig.dmgm.model;

import org.biiig.dmgm.api.model.DirectedGraphFactory;
import org.biiig.dmgm.impl.model.AdjacencyListFactory;
import org.biiig.dmgm.impl.model.DFSCodeFactory;

public class DFSCodeTest extends DirectedGraphTest {

  @Override
  DirectedGraphFactory getFactory() {
    return new DFSCodeFactory();
  }
}