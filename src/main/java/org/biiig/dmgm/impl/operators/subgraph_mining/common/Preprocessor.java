package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.api.GraphCollection;

import java.util.function.BiFunction;

public interface Preprocessor
  extends BiFunction<GraphCollection, GraphCollectionBuilder, GraphCollection> {
}
