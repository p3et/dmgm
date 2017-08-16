package org.biiig.dmgm.impl.algorithms.tfsm;

import com.google.common.collect.Lists;
import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.model.graph.DirectedGraph;
import org.biiig.dmgm.impl.model.graph.DFSCode;
import org.biiig.dmgm.todo.gspan.DFSEmbedding;
import org.biiig.dmgm.todo.gspan.GSpanTreeNode;
import org.biiig.dmgm.todo.gspan.GraphDFSEmbeddings;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SingleEdgePatternReporter implements RunnableFuture<List<GSpanTreeNode>> {
  private final Database database;
  private final Queue<Integer> graphIdQueue;
  private final List<GSpanTreeNode> graphReports = Lists.newLinkedList();
  private final List<GSpanTreeNode> partitionReports = Lists.newLinkedList();
  private boolean cancelled = false;
  private boolean done = false;


  public SingleEdgePatternReporter(Database database, Queue<Integer> graphIdQueue) {

    this.database = database;
    this.graphIdQueue = graphIdQueue;
  }

  @Override
  public void run() {
    while (!graphIdQueue.isEmpty() && !isCancelled()) {
      Integer graphId = graphIdQueue.poll();

      if (graphId != null) {
        graphReports.clear();

        DirectedGraph graph = database.getGraph(graphId);

        for (int edgeId = 0; edgeId < graph.getEdgeCount(); edgeId++) {

          int sourceId = graph.getSourceId(edgeId);
          int targetId = graph.getTargetId(edgeId);
          boolean loop = sourceId == targetId;

          int fromTime = 0;
          int toTime = loop ? 0 : 1;

          int fromLabel;
          boolean outgoing;
          int edgeLabel = graph.getEdgeLabel(edgeId);
          int toLabel;

          int fromId;
          int toId;

          int sourceLabel = graph.getVertexLabel(sourceId);
          int targetLabel = graph.getVertexLabel(targetId);

          if (sourceLabel <= targetLabel) {
            fromId = sourceId;
            fromLabel = sourceLabel;

            outgoing = true;

            toId = targetId;
            toLabel = sourceLabel;
          } else {
            fromId = targetId;
            fromLabel = targetLabel;

            outgoing = false;

            toId = sourceId;
            toLabel = sourceLabel;
          }


          DFSCode dfsCode = new DFSCode(
            fromTime, toTime, fromLabel, outgoing, edgeLabel, toLabel);

          DFSEmbedding embedding = new DFSEmbedding(fromId, edgeId, toId);

          GraphDFSEmbeddings embeddings = new GraphDFSEmbeddings(graphId, embedding);

          graphReports.add(new GSpanTreeNode(dfsCode, embeddings));
        }

        GSpanTreeNode.aggregateForGraph(graphReports);
        partitionReports.addAll(graphReports);
      }
    }
    GSpanTreeNode.aggregate(partitionReports);
    done = true;
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    this.cancelled = true;
    return isCancelled();
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public boolean isDone() {
    return done;
  }

  @Override
  public List<GSpanTreeNode> get() throws InterruptedException, ExecutionException {
    while (!done) {
      Thread.sleep(100);
    }

    return partitionReports;
  }

  @Override
  public List<GSpanTreeNode> get(long timeout, TimeUnit unit) throws InterruptedException,
    ExecutionException, TimeoutException {
    return get();
  }
}
