package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.api.GraphDB;

public interface FilterAndOutputFactory  {
  FilterOrOutput create(GraphDB db);
}
