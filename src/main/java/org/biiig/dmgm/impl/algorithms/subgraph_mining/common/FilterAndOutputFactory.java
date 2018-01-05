package org.biiig.dmgm.impl.algorithms.subgraph_mining.common;

import org.biiig.dmgm.api.GraphCollection;

public interface FilterAndOutputFactory  {
  FilterAndOutput create(GraphCollection output);
}
