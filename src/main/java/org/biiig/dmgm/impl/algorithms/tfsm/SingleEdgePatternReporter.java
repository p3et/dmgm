package org.biiig.dmgm.impl.algorithms.tfsm;

import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.model.graph.DirectedGraph;
import org.biiig.dmgm.todo.gspan.GSpanTreeNode;

import java.util.Collection;
import java.util.List;
import java.util.Queue;

public class SingleEdgePatternReporter implements Runnable {
  private final Database database;
  private final Queue<Integer> graphIdQueue;
  private final Collection<List<GSpanTreeNode>> reports;

  public SingleEdgePatternReporter(Database database, Queue<Integer> graphIdQueue,
    Collection<List<GSpanTreeNode>> reports) {

    this.database = database;
    this.graphIdQueue = graphIdQueue;
    this.reports = reports;
  }

  @Override
  public void run() {
    while (!graphIdQueue.isEmpty()) {
      Integer graphId = graphIdQueue.poll();

      if (graphId != null) {
        DirectedGraph graph = database.getGraph(graphId);

        System.out.println(graph);
      }
    }
  }
}
