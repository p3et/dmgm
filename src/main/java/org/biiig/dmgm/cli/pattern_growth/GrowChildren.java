package org.biiig.dmgm.cli.pattern_growth;

import org.biiig.dmgm.api.model.graph.IntGraph;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSCodeEmbeddingPair;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSEmbedding;
import org.biiig.dmgm.impl.model.graph.DFSCode;

public interface GrowChildren {
  DFSCodeEmbeddingPair[] apply(IntGraph graph, DFSCode parentCode, int[] rightmostPath, DFSEmbedding parentEmbedding);
}
