package org.biiig.dmgm.cli;

import org.biiig.dmgm.api.HyperVertexOperator;
import org.biiig.dmgm.impl.operators.subgraph_mining.FrequentSubgraphs;
import org.biiig.dmgm.impl.loader.TLFLoader;

import java.io.IOException;

public class FrequentSubgraphMiner {

  public static void main(String[] args) throws IOException {

    String path =
      "/home/peet/pred_10K.tlf";
    GraphCollection input = TLFLoader
      .fromFile(path)
      .getGraphCollection();

    HyperVertexOperator fsm = new FrequentSubgraphs(1.0f, 20);

    GraphCollection output = input.apply(fsm);

    System.out.println(GraphCollection.toString(output));
  }
}
