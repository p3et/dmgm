package org.biiig.dmgm.impl.graph;

import org.biiig.dmgm.api.GraphFactory;

public class SourceTargetMuxTest extends SingleLabelDirectedGraphTest {

  @Override
  GraphFactory getFactory() {
    return new GraphBaseFactory();
  }
}