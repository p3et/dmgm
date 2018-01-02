package org.biiig.dmgm.impl.algorithms.tfsm;

import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.impl.graph.DFSCode;

public interface PatternGrower {
  DFSCodeEmbeddingPair[] growChildDFSCodes(Graph graph, DFSCode parentCode,
                                           DFSEmbedding[] parentEmbeddings);
}
