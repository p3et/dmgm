package org.biiig.dmgm.impl.algorithms.tfsm;

import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.impl.model.graph.DFSCode;

public interface PatternGrower {
  DFSCodeEmbeddingPair[] growChildren(DMGraph graph, DFSCode parentCode,
    DFSEmbedding[] parentEmbeddings);
}
