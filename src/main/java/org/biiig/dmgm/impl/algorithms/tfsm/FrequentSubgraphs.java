package org.biiig.dmgm.impl.algorithms.tfsm;

import org.biiig.dmgm.api.algorithms.tfsm.Operator;
import org.biiig.dmgm.api.model.collection.GraphCollection;
import org.biiig.dmgm.api.model.graph.IntGraph;
import org.biiig.dmgm.impl.algorithms.tfsm.concurrency.DFSTreeTraverserFactory;
import org.biiig.dmgm.impl.algorithms.tfsm.concurrency.DFSTreeInitializerFactory;
import org.biiig.dmgm.impl.algorithms.tfsm.logic.DFSTreeNodeAggregator;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSTreeNode;
import org.biiig.dmgm.impl.concurrency.ConcurrencyUtil;
import org.biiig.dmgm.impl.model.collection.InMemoryGraphCollection;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Directed Multigraph gSpan
 */
public class FrequentSubgraphs implements Operator {

  private float minSupportRel;
  private int maxEdgeCount;

  public FrequentSubgraphs() {
    this.minSupportRel = 1.0f;
    this.maxEdgeCount = 5;
  }

  @Override
  public GraphCollection apply(GraphCollection inputCollection) {
    int minSupportAbs = Math.round((float) inputCollection.size() * this.minSupportRel);

    Set<Integer> frequentVertexLabels = getFrequentLabels(
      inputCollection
        .parallelStream()
        .flatMap(new DistinctVertexLabels()),
      minSupportAbs);

    GraphCollection vertexPrunedCollection = new InMemoryGraphCollection();

    inputCollection
      .parallelStream()
      .map(new PruneVertices(frequentVertexLabels))
      .forEach(g -> vertexPrunedCollection.store(g));

    Set<Integer> frequentEdgeLabels = getFrequentLabels(
      inputCollection
        .parallelStream()
        .flatMap(new DistinctEdgeLabels()),
      minSupportAbs);

    GraphCollection prunedCollection = new InMemoryGraphCollection()
      .withVertexDictionary(inputCollection.getVertexDictionary())
      .withEdgeDictionary(inputCollection.getEdgeDictionary());

    vertexPrunedCollection
      .parallelStream()
      .map(new PruneVertices(frequentEdgeLabels))
      .forEach(g -> prunedCollection.store(g));

//    LabelDictionary vertexLabelDictionary = new InMemoryLabelDictionary(frequentVertexLabels);
//
//    Collection<String> frequentEdgeLabels = getFrequentLabels(
//      graphCollection
//        .parallelStream()
//        .flatMap(new DistinctEdgeLabels())
//    );

    System.out.println(prunedCollection);

    return prunedCollection;
  }

  private Set<Integer> getFrequentLabels(Stream<Integer> vertexLabels, int minSupportAbs) {
    return vertexLabels
      .collect(Collectors.groupingByConcurrent(Function.identity(), Collectors.counting()))
      .entrySet()
      .stream()
      .filter(e -> e.getValue() >= minSupportAbs)
      .map(Map.Entry::getKey)
      .collect(Collectors.toSet());
  }

  @Override
  public void execute(GraphCollection input, GraphCollection output) {

    // calculate min support
    int minSupportAbs = Math.round((float) input.size() * this.minSupportRel);

    Deque<DFSTreeNode> dfsTree = new ConcurrentLinkedDeque<>();

    // generate and report single edge patterns for each graph
    Deque<Integer> graphIdQueue = new ConcurrentLinkedDeque<>();
    for (int graphId = 0; graphId < input.size(); graphId++) {
      graphIdQueue.add(graphId);
    }

    Collection<List<DFSTreeNode>> dfsTreePartitions = ConcurrencyUtil
      .runParallel(new DFSTreeInitializerFactory(input, graphIdQueue));

    // aggregate partitions in parallel
    dfsTreePartitions.parallelStream()
      .forEach(children -> new DFSTreeNodeAggregator().aggregate(children));

    // combine partition results and aggregate globally
    List<DFSTreeNode> children = combine(dfsTreePartitions);
    children = new DFSTreeNodeAggregator().aggregate(children);

    // remove infrequent patterns
    children.removeIf(c -> c.getSupport() < minSupportAbs);

    // init DFS code tree
    dfsTree.addAll(children);

    // grow children until all frequent children in the tree were traversed
    Collection<List<IntGraph>> resultPartitions = ConcurrencyUtil
      .runParallel(new DFSTreeTraverserFactory(input, dfsTree, minSupportAbs));

    // add partition results to output
    for (IntGraph graph : combine(resultPartitions)) {
      output.store(graph);
    }

    // transfer input dictionaries to output ones
    output.withVertexDictionary(input.getVertexDictionary());
    output.withEdgeDictionary(input.getEdgeDictionary());
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

  public FrequentSubgraphs withMinSupport(float minSupport) {
    setMinSupportRel(minSupport);
    return this;
  }

  public FrequentSubgraphs withMaxEdgeCount(int maxEdgeCount) {
    setMaxEdgeCount(maxEdgeCount);
    return this;
  }

  public void setMinSupportRel(float minSupportRel) {
    this.minSupportRel = minSupportRel;
  }

  public void setMaxEdgeCount(int maxEdgeCount) {
    this.maxEdgeCount = maxEdgeCount;
  }

}
