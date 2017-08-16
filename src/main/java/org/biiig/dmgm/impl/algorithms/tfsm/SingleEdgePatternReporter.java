package org.biiig.dmgm.impl.algorithms.tfsm;

import com.google.common.collect.Lists;
import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.concurrency.TaskWithOutput;
import org.biiig.dmgm.api.model.graph.DirectedGraph;
import org.biiig.dmgm.impl.concurrency.DequeUpdateTask;
import org.biiig.dmgm.impl.model.graph.DFSCode;
import org.biiig.dmgm.todo.gspan.DFSEmbedding;
import org.biiig.dmgm.todo.gspan.GSpanTreeNode;
import org.biiig.dmgm.todo.gspan.GraphDFSEmbeddings;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SingleEdgePatternReporter
  extends DequeUpdateTask<Integer> implements TaskWithOutput<List<GSpanTreeNode>> {

  private final Database database;

  private final List<GSpanTreeNode> output = Lists.newLinkedList();
  private final List<GSpanTreeNode> supportedNodes = Lists.newLinkedList();
  private final Collection<Integer> emptyCollection = Lists.newArrayListWithCapacity(0);

  public SingleEdgePatternReporter(
    AtomicInteger activeCount, Deque<Integer> deque, Database database) {
    super(deque, activeCount);
    this.database = database;
  }


  @Override
  public List<GSpanTreeNode> getOutput() {
    return output;
  }

  @Override
  protected Collection<Integer> process(Integer graphId) {
    supportedNodes.clear();

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

      supportedNodes.add(new GSpanTreeNode(dfsCode, embeddings));
    }

    GSpanTreeNode.aggregateForGraph(supportedNodes);
    output.addAll(supportedNodes);

    return emptyCollection;
  }
}
