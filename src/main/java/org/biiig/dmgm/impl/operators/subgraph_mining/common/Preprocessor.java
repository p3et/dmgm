package org.biiig.dmgm.impl.operators.subgraph_mining.common;


import org.biiig.dmgm.api.HyperVertexDB;
import org.biiig.dmgm.api.SmallGraph;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

public interface Preprocessor
  extends BiFunction<List<SmallGraph>, HyperVertexDB, List<SmallGraph>> {
}
