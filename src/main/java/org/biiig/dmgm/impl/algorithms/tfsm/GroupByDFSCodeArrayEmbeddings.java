package org.biiig.dmgm.impl.algorithms.tfsm;

import de.jesemann.paralleasy.collectors.GroupByKeyArrayValues;
import org.biiig.dmgm.impl.graph.DFSCode;

public class GroupByDFSCodeArrayEmbeddings
  extends GroupByKeyArrayValues<DFSCodeEmbeddingPair, DFSCode, DFSEmbedding> {

  public GroupByDFSCodeArrayEmbeddings() {
    super(DFSCodeEmbeddingPair::getDfsCode, DFSCodeEmbeddingPair::getEmbedding, DFSEmbedding.class);
  }
}