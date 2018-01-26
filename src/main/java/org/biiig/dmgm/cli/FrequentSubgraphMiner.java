//package org.biiig.dmgm.cli;
//
//import org.biiig.dmgm.api.HyperVertexOperator;
//import org.biiig.dmgm.api.SmallGraph;
//import org.biiig.dmgm.impl.operators.subgraph_mining.FrequentSubgraphs;
//import org.biiig.dmgm.impl.loader.TLFLoader;
//
//import java.io.IOException;
//import java.util.List;
//
//public class FrequentSubgraphMiner {
//
//  public static void main(String[] args) throws IOException {
//
//    String path =
//      "/home/peet/pred_10K.tlf";
//    List<SmallGraph> input = TLFLoader
//      .fromFile(path)
//      .getGraphCollection();
//
//    HyperVertexOperator fsm = new FrequentSubgraphs(1.0f, 20);
//
//    List<SmallGraph> output = input.apply(fsm);
//
//    System.out.println(GraphCollection.toString(output));
//  }
//}
