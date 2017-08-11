package org.biiig.dmgm.impl.algorithms.tfsm;

import com.google.common.collect.Lists;
import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.model.graph.DirectedGraph;
import org.biiig.dmgm.impl.model.graph.DFSCode;
import org.biiig.dmgm.todo.gspan.DFSEmbedding;
import org.biiig.dmgm.todo.gspan.GSpanTreeNode;
import org.biiig.dmgm.todo.gspan.GraphDFSEmbeddings;
import org.biiig.dmgm.todo.model.labeled_graph.LabeledAdjacencyListEntry;

import java.util.Collection;
import java.util.List;
import java.util.Queue;

public class SingleEdgePatternReporter implements Runnable {
  private final Database database;
  private final Queue<Integer> graphIdQueue;
  private final Collection<List<GSpanTreeNode>> globalReports;
  private final List<GSpanTreeNode> localReports = Lists.newLinkedList();

  public SingleEdgePatternReporter(Database database, Queue<Integer> graphIdQueue,
    Collection<List<GSpanTreeNode>> reports) {

    this.database = database;
    this.graphIdQueue = graphIdQueue;
    this.globalReports = reports;
  }

  @Override
  public void run() {
    while (!graphIdQueue.isEmpty()) {
      Integer graphId = graphIdQueue.poll();

      if (graphId != null) {
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

          localReports.add(new GSpanTreeNode(dfsCode, embeddings));
        }
      }
    }

    GSpanTreeNode.aggregateForGraph(localReports);
    globalReports.add(localReports);
  }
}
