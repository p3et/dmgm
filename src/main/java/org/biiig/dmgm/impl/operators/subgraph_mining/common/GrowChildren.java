package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.impl.graph.DFSCode;

public interface GrowChildren {
  DFSCodeEmbeddingPair[] apply(CachedGraph graph, DFSCode parentCode, int[] rightmostPath, DFSEmbedding parentEmbedding);
}
