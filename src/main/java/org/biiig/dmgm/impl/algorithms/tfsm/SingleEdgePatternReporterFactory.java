package org.biiig.dmgm.impl.algorithms.tfsm;

import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.concurrency.TaskWithOutput;
import org.biiig.dmgm.api.concurrency.TaskWithOutputFactory;
import org.biiig.dmgm.todo.gspan.GSpanTreeNode;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class SingleEdgePatternReporterFactory implements TaskWithOutputFactory<List<GSpanTreeNode>> {

  private final Database database;
  private final Deque<Integer> deque;

  public SingleEdgePatternReporterFactory(Database database, Deque<Integer> deque) {
    this.database = database;
    this.deque = deque;
  }

  @Override
  public TaskWithOutput<List<GSpanTreeNode>> create(AtomicInteger activeCount) {
    return new SingleEdgePatternReporter(activeCount, deque, database);
  }
}
