package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.api.GraphCollection;

public interface FilterAndOutputFactory  {
  FilterOrOutput create(GraphCollection output);
}
