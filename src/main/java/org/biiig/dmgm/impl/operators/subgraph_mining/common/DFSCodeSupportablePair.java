package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import java.util.Collection;

public interface DFSCodeSupportablePair {
  DFSCode getDFSCode();
  Collection<DFSEmbedding> getEmbeddings();
}
