package org.biiig.dmgm.impl.algorithms.tfsm;

import de.jesemann.queue_stream.util.GroupBySelectorArraySelector;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSCodeEmbeddingPair;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSEmbedding;
import org.biiig.dmgm.impl.model.graph.DFSCode;

public class GroupByDFSCodeArrayEmbeddings
  extends GroupBySelectorArraySelector<DFSCodeEmbeddingPair, DFSCode, DFSEmbedding> {

  public GroupByDFSCodeArrayEmbeddings() {
    super(DFSCodeEmbeddingPair::getDfsCode, DFSCodeEmbeddingPair::getEmbedding, DFSEmbedding.class);
  }
}