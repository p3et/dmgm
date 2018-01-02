package org.biiig.dmgm.impl.algorithms.tfsm;

import de.jesemann.paralleasy.collectors.GroupByKeyArrayValues;
import org.biiig.dmgm.impl.graph.DFSCode;

public class GroupByGraphIdArrayEmbeddings
  extends GroupByKeyArrayValues<DFSEmbedding, Integer, DFSEmbedding> {

  public GroupByGraphIdArrayEmbeddings() {
    super(DFSEmbedding::getGraphId, e -> e, DFSEmbedding.class);
  }
}