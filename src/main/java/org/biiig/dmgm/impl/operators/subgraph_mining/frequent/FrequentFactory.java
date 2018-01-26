package org.biiig.dmgm.impl.operators.subgraph_mining.frequent;

import org.biiig.dmgm.api.HyperVertexDB;
import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterAndOutputFactory;

import java.util.List;

public class FrequentFactory implements FilterAndOutputFactory {

  private final int minSupportAbsolute;

  public FrequentFactory(int minSupportAbsolute) {
    this.minSupportAbsolute = minSupportAbsolute;
  }

  @Override
  public FilterOrOutput create(HyperVertexDB db) {
    return new FrequentFilter(minSupportAbsolute);
  }
}
