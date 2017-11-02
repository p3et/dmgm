package org.biiig.dmgm.impl.algorithms.tfsm;

import com.google.common.collect.Lists;
import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.api.concurrency.TaskWithOutput;
import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.impl.concurrency.DequeUpdateTask;
import org.biiig.dmgm.impl.model.graph.DFSCode;
import org.biiig.dmgm.todo.gspan.DFSEmbedding;
import org.biiig.dmgm.todo.gspan.DFSTreeNode;
import org.biiig.dmgm.todo.gspan.GraphDFSEmbeddings;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SingleEdgeNodeCreator
  extends DequeUpdateTask<Integer> implements TaskWithOutput<List<DFSTreeNode>> {

  private final DMGraphCollection database;

  private final List<DFSTreeNode> output = Lists.newLinkedList();
  private final List<DFSTreeNode> supportedNodes = Lists.newLinkedList();
  private final Collection<Integer> emptyCollection = Lists.newArrayListWithCapacity(0);

  public SingleEdgeNodeCreator(
    AtomicInteger activeCount, Deque<Integer> deque, DMGraphCollection database) {
    super(deque, activeCount);
    this.database = database;
  }


  @Override
  public List<DFSTreeNode> getOutput() {
    return output;
  }

  @Override
  protected Collection<Integer> process(Integer graphId) {
    supportedNodes.clear();

    DMGraph graph = database.getGraph(graphId);

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
        toLabel = targetLabel;
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

      supportedNodes.add(new DFSTreeNode(dfsCode, embeddings));
    }

    DFSTreeNode.aggregateForGraph(supportedNodes);
    output.addAll(supportedNodes);

    return emptyCollection;
  }
}
