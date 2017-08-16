package org.biiig.dmgm.impl.algorithms.tfsm;

import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.concurrency.PartitionTaskFactory;
import org.biiig.dmgm.todo.gspan.GSpanTreeNode;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.RunnableFuture;


public class SingleEdgePatternReporterFactory implements
  PartitionTaskFactory<Integer, List<GSpanTreeNode>> {

  private final Database database;

  public SingleEdgePatternReporterFactory(Database database) {
    this.database = database;
  }

  @Override
  public RunnableFuture<List<GSpanTreeNode>> create(Queue<Integer> queue) {
    return new SingleEdgePatternReporter(database, queue);
  }
}
