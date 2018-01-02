package org.biiig.dmgm.impl.algorithms.fsm.fsm;

import de.jesemann.paralleasy.collectors.GroupByKeyArrayValues;

public class GroupByGraphIdArrayEmbeddings
  extends GroupByKeyArrayValues<DFSEmbedding, Integer, DFSEmbedding> {

  public GroupByGraphIdArrayEmbeddings() {
    super(DFSEmbedding::getGraphId, e -> e, DFSEmbedding.class);
  }
}