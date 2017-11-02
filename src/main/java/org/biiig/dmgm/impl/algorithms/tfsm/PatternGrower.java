package org.biiig.dmgm.impl.algorithms.tfsm;

import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.impl.model.graph.DFSCode;

import java.util.List;

public interface PatternGrower {
  List<DFSCodeEmbeddingPair> growChildren(DMGraph graph, DFSCode parentCode,
    DFSEmbedding[] parentEmbeddings);
}
