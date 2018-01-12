package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.impl.graph.DFSCode;

public interface GrowChildren {
  DFSCodeEmbeddingPair[] apply(Graph graph, DFSCode parentCode, int[] rightmostPath, DFSEmbedding parentEmbedding);
}
