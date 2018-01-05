package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSCodeEmbeddingsPair;

import java.util.function.Consumer;

public class VectorMiner implements Consumer<DFSCodeEmbeddingsPair> {

  private final GraphCollection output;

  public VectorMiner(GraphCollection output) {
    this.output = output;
  }

  @Override
  public void accept(DFSCodeEmbeddingsPair pair) {

  }


}
