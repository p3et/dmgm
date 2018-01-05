package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutput;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutputFactory;

public class FrequentGeneralizationFactory implements FilterAndOutputFactory {
  private final int minSupportAbsolute;

  public FrequentGeneralizationFactory(int minSupportAbsolute) {
    this.minSupportAbsolute = minSupportAbsolute;
  }

  @Override
  public FilterAndOutput create(GraphCollection output) {
    return new GeneralizedFrequent(output, minSupportAbsolute);
  }
}
