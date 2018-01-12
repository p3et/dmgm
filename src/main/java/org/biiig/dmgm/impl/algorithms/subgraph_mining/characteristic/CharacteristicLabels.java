package org.biiig.dmgm.impl.algorithms.subgraph_mining.characteristic;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.GraphCollectionBuilder;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.Preprocessor;

public class CharacteristicLabels implements Preprocessor {
  @Override
  public GraphCollection apply(GraphCollection collection, GraphCollectionBuilder builder) {
    return collection;
  }
}
