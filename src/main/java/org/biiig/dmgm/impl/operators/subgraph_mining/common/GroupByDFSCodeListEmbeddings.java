package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import org.biiig.dmgm.impl.operators.fsm.common.DFSCode;
import org.biiig.dmgm.impl.operators.fsm.common.DFSEmbedding;

class GroupByDFSCodeListEmbeddings
  extends GroupByKeyListValues<DFSCodeEmbeddingPair, DFSCode, DFSEmbedding> {

  GroupByDFSCodeListEmbeddings() {
    super(DFSCodeEmbeddingPair::getDfsCode, DFSCodeEmbeddingPair::getEmbedding);
  }
}