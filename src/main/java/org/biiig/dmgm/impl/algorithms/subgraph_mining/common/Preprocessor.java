package org.biiig.dmgm.impl.algorithms.subgraph_mining.common;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.GraphCollectionBuilder;

import java.util.function.BiFunction;

public interface Preprocessor
  extends BiFunction<GraphCollection, GraphCollectionBuilder, GraphCollection> {
}
