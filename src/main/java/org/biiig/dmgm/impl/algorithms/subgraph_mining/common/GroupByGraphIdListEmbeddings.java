package org.biiig.dmgm.impl.algorithms.subgraph_mining.common;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;

class GroupByGraphIdListEmbeddings
  extends GroupByKeyListValues<DFSEmbedding, Integer, DFSEmbedding> {

  GroupByGraphIdListEmbeddings() {
    super(DFSEmbedding::getGraphId, e -> e);
  }
}