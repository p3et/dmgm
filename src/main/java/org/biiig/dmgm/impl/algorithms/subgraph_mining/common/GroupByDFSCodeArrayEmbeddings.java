package org.biiig.dmgm.impl.algorithms.subgraph_mining.common;

import de.jesemann.paralleasy.collectors.GroupByKeyArrayValues;
import org.biiig.dmgm.impl.graph.DFSCode;

class GroupByDFSCodeArrayEmbeddings
  extends GroupByKeyArrayValues<DFSCodeEmbeddingPair, DFSCode, DFSEmbedding> {

  GroupByDFSCodeArrayEmbeddings() {
    super(DFSCodeEmbeddingPair::getDfsCode, DFSCodeEmbeddingPair::getEmbedding, DFSEmbedding.class);
  }
}