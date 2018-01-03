package org.biiig.dmgm.impl.algorithms.subgraph_mining.fsm;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutput;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutputFactory;

public class FrequentFactory implements FilterAndOutputFactory {
  @Override
  public FilterAndOutput create(int minSupportAbs, GraphCollection output) {
    return new Frequent(minSupportAbs, output);
  }
}
