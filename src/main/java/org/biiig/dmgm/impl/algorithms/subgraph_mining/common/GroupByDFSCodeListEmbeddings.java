package org.biiig.dmgm.impl.algorithms.subgraph_mining.common;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import org.biiig.dmgm.impl.graph.DFSCode;

class GroupByDFSCodeListEmbeddings
  extends GroupByKeyListValues<DFSCodeEmbeddingPair, DFSCode, DFSEmbedding> {

  GroupByDFSCodeListEmbeddings() {
    super(DFSCodeEmbeddingPair::getDfsCode, DFSCodeEmbeddingPair::getEmbedding);
  }
}