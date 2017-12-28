package org.biiig.dmgm.cli;

import org.biiig.dmgm.impl.model.source.tlf.TLFDataSource;

import java.io.IOException;

public class FrequentSubgraphMiner {

  public static void main(String[] args) throws IOException {

    String path = "/home/peet/pred_10K.tlf";

    StringGraphCollection input = TLFDataSource
      .fromFile(path)
      .getGraphCollection();

//    Algorithm fsm = new FrequentSubgraphs()
//      .withMinSupport(1.0f);
//
//    GraphCollection output = input
//      .apply(fsm);

    System.out.println(input.size());
  }
}
