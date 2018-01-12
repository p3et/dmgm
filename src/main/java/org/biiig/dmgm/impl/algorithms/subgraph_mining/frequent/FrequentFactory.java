package org.biiig.dmgm.impl.algorithms.subgraph_mining.frequent;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutputFactory;

public class FrequentFactory implements FilterAndOutputFactory {
  private final int minSupportAbsolute;

  public FrequentFactory(int minSupportAbsolute) {
    this.minSupportAbsolute = minSupportAbsolute;
  }

  @Override
  public FilterOrOutput create(GraphCollection output) {
    return new FrequentFilter(minSupportAbsolute);
  }
}
