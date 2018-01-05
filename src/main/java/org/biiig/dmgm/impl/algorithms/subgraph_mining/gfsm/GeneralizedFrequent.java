package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.fsm.Frequent;

public class GeneralizedFrequent extends Frequent {

  GeneralizedFrequent(GraphCollection output, int minSupportAbsolute) {
    super(output, minSupportAbsolute);
  }

  @Override
  public boolean test(DFSCodeEmbeddingsPair pairs) {
    System.out.println("GVM");
    return super.test(pairs);
  }

  //  @Override
//  public boolean test(DFSCodeEmbeddingsPair pairs) {
//    DFSEmbedding[] embeddings = pairs.getEmbeddings();
//
//    int frequency = embeddings.length;
//    int support = frequency >= minSupport ? getSupport(embeddings) : 0;
//
//    boolean frequent = support >= minSupport;
//
//    if (frequent) {
//      store(pairs.getDfsCode(), frequency, support);
//
//      List<MultiDimensionalVector> vectors = Stream.of(embeddings)
//        .map(new ToMultiDimensionalVector(output.getElementDataStore()))
//        .collect(Collectors.toList());
//
//      for (frequentSpecializatio.apply(vectors))
//    }
//
//    return frequent;
//  }
//
//  private void store(DFSCode dfsCode, int frequency, int support) {
//    int graphId = output.add(dfsCode);
//    output.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.SUPPORT, support);
//    output.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.FREQUENCY, frequency);
//  }
//
//  private void store(DFSCode dfsCode, MultiDimensionalVector vector, int frequency, int support) {
//    Graph graph = new GraphBase();
//
//    dfsCode
//      .vertexIdStream()
//      .forEach(dim -> graph
//        .addVertex(
//          vector.getSpecializedValue(dim)
//            .orElse(dfsCode.getVertexLabel(dim))
//        ));
//
//    dfsCode
//      .edgeIdStream()
//      .forEach(e ->
//        graph.addEdge(
//          dfsCode.getSourceId(e),
//          dfsCode.getTargetId(e),
//          dfsCode.getFromTime(e)
//        ));
//
//    int graphId = output.add(graph);
//    output.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.SUPPORT, support);
//    output.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.FREQUENCY, frequency);
//  }
//

//  @SuppressWarnings("unchecked")
//  private void outputFrequentSpecializations(DFSCodeEmbeddingsPair topLevelPairs) {
//    DFSCode topLevel = topLevelPairs.getDfsCode();
//
//
//
//    QueueStreamSource<MultiDimensionalVector> queue = QueueStreamSource.of(vectors);
//
//    queue
//      .stream()
//      .flatMap(new AllSpecializations(topLevel.getVertexCount()))
//      .collect(Collectors.groupingBy(Function.identity(), Collectors.toList()))
//      .entrySet()
//      .stream()
//      .filter(e -> e.getValue().size() > minSupport)
//      // add one new graph per frequent specialization vector
//      .peek(e -> outputSpecialization(topLevel, e))
//      // but add all instances to queue
//      .flatMap(e -> e.getValue().stream())
//      .forEach(queue::add);
//  }
}
