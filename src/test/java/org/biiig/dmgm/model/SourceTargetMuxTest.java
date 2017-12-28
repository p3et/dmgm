package org.biiig.dmgm.model;

import org.biiig.dmgm.api.model.graph.IntGraphFactory;
import org.biiig.dmgm.impl.model.graph.SourceTargetMuxFactory;

public class SourceTargetMuxTest extends SingleLabelDirectedGraphTest {

  @Override
  IntGraphFactory getFactory() {
    return new SourceTargetMuxFactory();
  }
}