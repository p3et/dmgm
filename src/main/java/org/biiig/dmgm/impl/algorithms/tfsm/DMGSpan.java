package org.biiig.dmgm.impl.algorithms.tfsm;

import org.biiig.dmgm.api.algorithms.tfsm.Algorithm;
import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.impl.concurrency.ConcurrencyUtil;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Directed Multigraph gSpan
 */
public class DMGSpan implements Algorithm {

  public static final int PARALLELISM = Runtime.getRuntime().availableProcessors();
  private final TFSMConfig config;

  public DMGSpan(TFSMConfig config) {
    this.config = config;
  }

  @Override
  public void execute(DMGraphCollection input, DMGraphCollection output) {

    // calculate min support
    int minSupport = Math.round((float) input.size() * config.getMinSupport());

    Deque<DFSTreeNode> dfsTree = new ConcurrentLinkedDeque<>();

    // generate and report single edge patterns for each graph
    Deque<Integer> graphIdQueue = new ConcurrentLinkedDeque<>();
    for (int graphId = 0; graphId < input.size(); graphId++) {
      graphIdQueue.add(graphId);
    }

    Collection<List<DFSTreeNode>> dfsTreePartitions = ConcurrencyUtil
      .runParallel(new SingleEdgeNodeCreatorFactory(input, graphIdQueue));

    // parallel aggregation
//    dfsTreePartitions.parallelStream().forEach(DFSTreeNode::aggregate);

    List<DFSTreeNode> children = combine(dfsTreePartitions);

    children = new DFSTreeNodeAggregator().aggregate(children);

    children.removeIf(c -> c.getSupport() < minSupport);
    dfsTree.addAll(children);

    Collection<List<DMGraph>> resultPartitions = ConcurrencyUtil
      .runParallel(new DFSTreeTraverserFactory(input, dfsTree));

    output.setVertexDictionary(input.getVertexDictionary());
    output.setEdgeDictionary(input.getEdgeDictionary());

    for (DMGraph graph : combine(resultPartitions)) {
      output.store(graph);
    }
  }

  private <T> List<T> combine(Collection<List<T>> reports) {

    // single thread combination
    Iterator<List<T>> iterator = reports.iterator();
    List<T> combination = iterator.next();
    while (iterator.hasNext()) {
      combination.addAll(iterator.next());
    }

    return combination;
  }

  private List<DFSTreeNode> getFrequentChildren(List<DFSTreeNode> children, int minSupport) {

    return children;
  }

  //  private void growForNextParent() {
//    children.clear();
//    GSpanTreeNode parent = parents.pollLast();
//
//    DFSCode dfsCode = parent.getDfsCode();
//    int support = 0;
//    int frequency = 0;
//
//    int[] rightmostPathTimes = getRightmostPathTimes(dfsCode);
//
//    for (GraphDFSEmbeddings graphEmbeddings : parent.getEmbeddings()) {
//      support++;
//      frequency += graphEmbeddings.getEmbeddings().length;
//
//      growChildDFSCodes(
//        database, graphEmbeddings, dfsCode, rightmostPathTimes);
//      GSpanTreeNode.aggregateForGraph(reports);
//      children.addAll(reports);
//    }
//    countAndPrune();
//  }
//
//  private void countAndPrune() {
//    GSpanTreeNode.reduce(children);
//
//    for (GSpanTreeNode node : children) {
//      int support = node.getSupport();
//      if (support >= minSupport) {
//        int size = node.getDfsCode().getEdgeCount();
//        if (size == 1 || isMinimal(node.getDfsCode())) {
//
//          if (size < kMax ) {
//            parents.add(node);
//          }
//
//          result.add(new Countable<>(node.getDfsCode(), node.getSupport(), node.getFrequency()));
//        }
//      }
//    }
//  }
//

//
//
//  private void reportSingleEdges(LabeledGraph graph) {
//    reports.clear();
//    int fromTime = 0;
//
//
//    graph.createAdjacencyList();
//

//    GSpanTreeNode.aggregateForGraph(reports);
//  }
//
//  private void createDictionaries(String input) throws IOException {
//    Stream<String> stream = Files.lines(Paths.get(input));
//    Iterator<String> iterator = stream.iterator();
//
//    final List<Countable<String>> vertexLabels = Lists.newArrayList();
//    final List<Countable<String>> edgeLabels = Lists.newArrayList();
//
//    final List<Countable<String>> graphVertexLabels = Lists.newArrayList();
//    final List<Countable<String>> graphEdgeLabels = Lists.newArrayList();
//
//    while (iterator.hasNext()) {
//      String line = iterator.next();
//      String[] fields = line.split(" ");
//
//      if (fields[0].equals("t")) {
//        graphCount++;
//
//        Countable.sumFrequency(graphVertexLabels);
//        vertexLabels.addAll(graphVertexLabels);
//        graphVertexLabels.clear();
//
//        Countable.sumFrequency(graphEdgeLabels);
//        edgeLabels.addAll(graphEdgeLabels);
//        graphEdgeLabels.clear();
//
//      } else if (fields[0].equals("e")) {
//        graphEdgeLabels.add(new Countable<>(fields[3]));
//      } else {
//        graphVertexLabels.add(new Countable<>(fields[2]));
//      }
//    }
//
//    Countable.sumFrequency(graphVertexLabels);
//    vertexLabels.addAll(graphVertexLabels);
//
//    Countable.sumFrequency(graphEdgeLabels);
//    edgeLabels.addAll(graphEdgeLabels);
//
//    createDictionaries(vertexLabels, edgeLabels);
//  }
//

//
//
//
//  public List<Countable<DFSCode>> getResult() {
//    return result;
//  }
//

}
