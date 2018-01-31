package org.biiig.dmgm.impl.operators.subgraph_mining.frequent;

import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterAndOutputFactory;

public class FrequentFactory implements FilterAndOutputFactory {

  private final int minSupportAbsolute;

  public FrequentFactory(int minSupportAbsolute) {
    this.minSupportAbsolute = minSupportAbsolute;
  }

  @Override
  public FilterOrOutput create(GraphDB db) {
    return new FrequentFilter(minSupportAbsolute);
  }
}
