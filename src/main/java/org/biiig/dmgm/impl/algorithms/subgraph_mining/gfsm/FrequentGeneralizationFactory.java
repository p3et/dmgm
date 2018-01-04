package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.biiig.dmgm.api.ElementDataStore;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutput;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutputFactory;

public class FrequentGeneralizationFactory implements FilterAndOutputFactory {
  private final ElementDataStore dataStore;

  public FrequentGeneralizationFactory(ElementDataStore dataStore) {
    this.dataStore = dataStore;
  }

  @Override
  public FilterAndOutput create(int minSupportAbs, GraphCollection output) {
    return new GeneralizedFrequent(minSupportAbs, output, dataStore);
  }
}
