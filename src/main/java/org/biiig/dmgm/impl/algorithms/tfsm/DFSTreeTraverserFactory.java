package org.biiig.dmgm.impl.algorithms.tfsm;

import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.concurrency.TaskWithOutput;
import org.biiig.dmgm.api.concurrency.TaskWithOutputFactory;
import org.biiig.dmgm.api.model.graph.DirectedGraph;
import org.biiig.dmgm.todo.gspan.DFSTreeNode;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by peet on 18.08.17.
 */
public class DFSTreeTraverserFactory implements TaskWithOutputFactory<List<DirectedGraph>> {
  private final Database database;
  private final Deque<DFSTreeNode> dfsTree;

  public DFSTreeTraverserFactory(Database database, Deque<DFSTreeNode> dfsTree) {
    this.database = database;
    this.dfsTree = dfsTree;
  }

  @Override
  public TaskWithOutput<List<DirectedGraph>> create(AtomicInteger activeCount) {
    return new DFSTreeTraverser(dfsTree, activeCount);
  }
}
