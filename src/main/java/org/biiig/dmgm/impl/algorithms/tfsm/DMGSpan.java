package org.biiig.dmgm.impl.algorithms.tfsm;

import org.biiig.dmgm.api.algorithms.tfsm.Algorithm;
import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.impl.algorithms.tfsm.concurrency.DFSTreeTraverserFactory;
import org.biiig.dmgm.impl.algorithms.tfsm.concurrency.DFSTreeInitializerFactory;
import org.biiig.dmgm.impl.algorithms.tfsm.logic.DFSTreeNodeAggregator;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSTreeNode;
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
      .runParallel(new DFSTreeInitializerFactory(input, graphIdQueue));

    // aggregate partitions in parallel
    dfsTreePartitions.parallelStream()
      .forEach(children -> new DFSTreeNodeAggregator().aggregate(children));

    // combine partition results and aggregate globally
    List<DFSTreeNode> children = combine(dfsTreePartitions);
    children = new DFSTreeNodeAggregator().aggregate(children);

    // remove infrequent patterns
    children.removeIf(c -> c.getSupport() < minSupport);

    // init DFS code tree
    dfsTree.addAll(children);

    // grow children until all frequent children in the tree were traversed
    Collection<List<DMGraph>> resultPartitions = ConcurrencyUtil
      .runParallel(new DFSTreeTraverserFactory(input, dfsTree, minSupport));

    // add partition results to output
    for (DMGraph graph : combine(resultPartitions)) {
      output.store(graph);
    }

    // transfer input dictionaries to output ones
    output.setVertexDictionary(input.getVertexDictionary());
    output.setEdgeDictionary(input.getEdgeDictionary());
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
}
