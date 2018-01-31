package org.biiig.dmgm.impl.operators.subgraph_mining.common;


import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.CachedGraph;

import java.util.List;
import java.util.function.BiFunction;

public interface Preprocessor
  extends BiFunction<List<CachedGraph>, GraphDB, List<CachedGraph>> {
}
