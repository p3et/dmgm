package org.biiig.dmgm.impl.algorithms.tfsm.concurrency;

import org.biiig.dmgm.api.model.collection.GraphCollection;
import org.biiig.dmgm.api.concurrency.TaskWithOutput;
import org.biiig.dmgm.api.concurrency.TaskWithOutputFactory;
import org.biiig.dmgm.api.model.graph.IntGraph;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSTreeNode;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by peet on 18.08.17.
 */
public class DFSTreeTraverserFactory implements TaskWithOutputFactory<List<IntGraph>> {
  private final GraphCollection input;
  private final Deque<DFSTreeNode> dfsTree;
  private final int minSupport;

  public DFSTreeTraverserFactory(GraphCollection input, Deque<DFSTreeNode> dfsTree,
                                 int minSupport) {
    this.input = input;
    this.dfsTree = dfsTree;
    this.minSupport = minSupport;
  }

  @Override
  public TaskWithOutput<List<IntGraph>> create(AtomicInteger activeCount) {
    return new DFSTreeTraverser(dfsTree, activeCount, input, minSupport);
  }
}
