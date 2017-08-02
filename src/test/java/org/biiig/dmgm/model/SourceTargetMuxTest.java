package org.biiig.dmgm.model;

import org.biiig.dmgm.api.model.DirectedGraphFactory;
import org.biiig.dmgm.impl.model.SourceTargetMuxFactory;

public class SourceTargetMuxTest extends DirectedGraphTest {

  @Override
  DirectedGraphFactory getFactory() {
    return new SourceTargetMuxFactory();
  }
}