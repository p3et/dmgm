package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.api.HyperVertexDB;

public interface FilterAndOutputFactory  {
  FilterOrOutput create(HyperVertexDB db);
}
