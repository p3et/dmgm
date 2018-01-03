package org.biiig.dmgm.impl.algorithms.subgraph_mining.common;

import de.jesemann.paralleasy.collectors.GroupByKeyArrayValues;

class GroupByGraphIdArrayEmbeddings
  extends GroupByKeyArrayValues<DFSEmbedding, Integer, DFSEmbedding> {

  GroupByGraphIdArrayEmbeddings() {
    super(DFSEmbedding::getGraphId, e -> e, DFSEmbedding.class);
  }
}