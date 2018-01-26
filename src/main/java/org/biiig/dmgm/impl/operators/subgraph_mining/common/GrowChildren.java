package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.impl.graph.DFSCode;

public interface GrowChildren {
  DFSCodeEmbeddingPair[] apply(SmallGraph graph, DFSCode parentCode, int[] rightmostPath, DFSEmbedding parentEmbedding);
}
