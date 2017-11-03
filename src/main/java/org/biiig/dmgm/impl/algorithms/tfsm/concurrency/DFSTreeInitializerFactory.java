package org.biiig.dmgm.impl.algorithms.tfsm.concurrency;

import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.api.concurrency.TaskWithOutput;
import org.biiig.dmgm.api.concurrency.TaskWithOutputFactory;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSTreeNode;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class DFSTreeInitializerFactory implements TaskWithOutputFactory<List<DFSTreeNode>> {

  private final DMGraphCollection database;
  private final Deque<Integer> deque;

  public DFSTreeInitializerFactory(DMGraphCollection database, Deque<Integer> deque) {
    this.database = database;
    this.deque = deque;
  }

  @Override
  public TaskWithOutput<List<DFSTreeNode>> create(AtomicInteger activeCount) {
    return new DFSTreeInitializer(activeCount, deque, database);
  }
}
