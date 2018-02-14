package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import org.biiig.dmgm.impl.operators.fsm.common.DFSEmbedding;

class GroupByGraphIdListEmbeddings
  extends GroupByKeyListValues<DFSEmbedding, Long, DFSEmbedding> {

  GroupByGraphIdListEmbeddings() {
    super(DFSEmbedding::getGraphId, e -> e);
  }
}