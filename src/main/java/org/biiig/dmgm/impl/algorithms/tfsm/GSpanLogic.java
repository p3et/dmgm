package org.biiig.dmgm.impl.algorithms.tfsm;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.impl.model.graph.DFSCode;

import java.util.List;

public class GSpanLogic {

  private final EdgeGrower outgoingGrower = new OutgoingEdgeGrower();
  private final EdgeGrower incomingGrower = new IncomingEdgeGrower();


  public void addSingleEdgeDFSTreeNodes(List<DFSTreeNode> supportedNodes, Integer graphId,
    DMGraph graph) {
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
  }



//  private DFSCode getMinDFSCode(LabeledGraph graph) {
//    graph.createAdjacencyList();
//
//    List<DFSTreeNode> supportedNodes = Lists.newLinkedList();
//    addSingleEdgeDFSTreeNodes(0, dfsCode, supportedNodes);
//
//    DFSTreeNode minParentNode = reports.get(0);
//
//    for (int i = 1; i < graph.getEdgeCount(); i++) {
//      DFSCode minDFSCode = minParentNode.getDfsCode();
//
//      GraphDFSEmbeddings[] embeddings = minParentNode.getEmbedding();
//      int[] rightmostPath = getRightmostPathTimes(minDFSCode);
//
//      growChildren(graph, embeddings[0], minDFSCode, rightmostPath);
//
//      minParentNode = reports.get(0);
//    }
//
//    return minParentNode.getDfsCode();
//  }

  private boolean isMinimal(DFSCode dfsCode) {
    List<DFSTreeNode> childNodes = Lists.newLinkedList();
    addSingleEdgeDFSTreeNodes(childNodes, 0, dfsCode);

    DFSTreeNode minParentNode = childNodes.get(0);
    boolean minimal = minParentNode.getDfsCode().parentOf(dfsCode);

    while (minimal) {
      DFSCode minDFSCode = minParentNode.getDfsCode();

      GraphDFSEmbeddings[] embeddings = minParentNode.getEmbeddings();
      int[] rightmostPath = getRightmostPathTimes(minDFSCode);

      growChildren(childNodes, minDFSCode, rightmostPath, graphId, dfsCode, embeddings[0]);

      DFSTreeNode.aggregateForGraph(childNodes);

      if (childNodes.isEmpty()) {
        break;
      } else {
        minParentNode = childNodes.get(0);
        minimal = minParentNode.getDfsCode().parentOf(dfsCode);
      }
    }

    return minimal;
  }

  public List<DFSCodeEmbeddingPair> growChildren(
    DMGraph graph, DFSCode parentCode, DFSEmbedding[] graphEmbeddings) {

    List<DFSCodeEmbeddingPair> pairs =
      outgoingGrower.growChildren(graph, parentCode, graphEmbeddings);

    pairs.addAll(incomingGrower.growChildren(graph, parentCode, graphEmbeddings));

    return pairs;
  }
}
