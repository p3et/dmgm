package org.biiig.dmgm.cli;

import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.api.model.collection.GraphCollection;
import org.biiig.dmgm.impl.algorithms.tfsm.FrequentSubgraphs;
import org.biiig.dmgm.impl.model.source.tlf.TLFLoader;

import java.io.IOException;

public class FrequentSubgraphMiner {

  public static void main(String[] args) throws IOException {

    String path =
//      "/home/peet/pred_10K.tlf";
      "/home/peet/git/dmgm/src/main/resources/samples/predictable.tlf";

    GraphCollection input = TLFLoader
      .fromFile(path)
      .getGraphCollection();

    Operator fsm = new FrequentSubgraphs(1.0f, 20);

    GraphCollection output = input.apply(fsm);
  }
}
