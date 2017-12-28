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
//    IntGraphCollection output = input
//      .apply(fsm);

   input
     .stream()
     .forEach(g -> System.out.println(g));
  }
}
