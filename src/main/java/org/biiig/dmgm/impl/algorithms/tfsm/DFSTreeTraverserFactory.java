package org.biiig.dmgm.impl.algorithms.tfsm;

import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.api.concurrency.TaskWithOutput;
import org.biiig.dmgm.api.concurrency.TaskWithOutputFactory;
import org.biiig.dmgm.api.model.graph.DMGraph;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by peet on 18.08.17.
 */
public class DFSTreeTraverserFactory implements TaskWithOutputFactory<List<DMGraph>> {
  private final DMGraphCollection input;
  private final Deque<DFSTreeNode> dfsTree;

  public DFSTreeTraverserFactory(DMGraphCollection input, Deque<DFSTreeNode> dfsTree) {
    this.input = input;
    this.dfsTree = dfsTree;
  }

  @Override
  public TaskWithOutput<List<DMGraph>> create(AtomicInteger activeCount) {
    return new DFSTreeTraverser(dfsTree, activeCount, input);
  }
}
