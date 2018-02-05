package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.Collection;

public interface DFSCodeSupportablePair {
  DFSCode getDFSCode();
  Collection<DFSEmbedding> getEmbeddings();
}