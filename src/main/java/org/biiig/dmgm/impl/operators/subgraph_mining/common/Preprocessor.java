package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import java.util.function.BiFunction;

public interface Preprocessor
  extends BiFunction<GraphCollection, GraphCollectionBuilder, GraphCollection> {
}
