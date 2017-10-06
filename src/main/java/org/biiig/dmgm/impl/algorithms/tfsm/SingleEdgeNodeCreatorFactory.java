package org.biiig.dmgm.impl.algorithms.tfsm;

import org.biiig.dmgm.api.DMGraphDatabase;
import org.biiig.dmgm.api.concurrency.TaskWithOutput;
import org.biiig.dmgm.api.concurrency.TaskWithOutputFactory;
import org.biiig.dmgm.todo.gspan.DFSTreeNode;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class SingleEdgeNodeCreatorFactory implements TaskWithOutputFactory<List<DFSTreeNode>> {

  private final DMGraphDatabase database;
  private final Deque<Integer> deque;

  public SingleEdgeNodeCreatorFactory(DMGraphDatabase database, Deque<Integer> deque) {
    this.database = database;
    this.deque = deque;
  }

  @Override
  public TaskWithOutput<List<DFSTreeNode>> create(AtomicInteger activeCount) {
    return new SingleEdgeNodeCreator(activeCount, deque, database);
  }
}
