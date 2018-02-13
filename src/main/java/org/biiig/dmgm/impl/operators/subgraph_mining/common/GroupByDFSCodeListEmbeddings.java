package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;

class GroupByDFSCodeListEmbeddings
  extends GroupByKeyListValues<DFSCodeEmbeddingPair, DFSCode, DFSEmbedding> {

  GroupByDFSCodeListEmbeddings() {
    super(DFSCodeEmbeddingPair::getDfsCode, DFSCodeEmbeddingPair::getEmbedding);
  }
}