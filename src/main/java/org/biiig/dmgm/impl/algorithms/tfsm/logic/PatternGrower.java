package org.biiig.dmgm.impl.algorithms.tfsm.logic;

import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSCodeEmbeddingPair;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSEmbedding;
import org.biiig.dmgm.impl.model.graph.DFSCode;

public interface PatternGrower {
  DFSCodeEmbeddingPair[] growChildDFSCodes(DMGraph graph, DFSCode parentCode,
    DFSEmbedding[] parentEmbeddings);
}
