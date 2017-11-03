package org.biiig.dmgm.impl.algorithms.tfsm.logic;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSCodeEmbeddingPair;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSEmbedding;
import org.biiig.dmgm.impl.model.graph.DFSCode;

import java.util.Arrays;

/**
 * Provides operations to create, extend and verify gSpan's DFS codes.
 */
public class DFSCodeOperations {

  /**
   * Logic to grow children of DFS codes by outgoing edges.
   */
  private static final PatternGrowerBase outgoingGrower = new OutgoingEdgePatternGrower();
  /**
   * Logic to grow children of DFS codes by incoming edges.
   */
  private static final PatternGrowerBase incomingGrower = new IncomingEdgePatternGrower();

  /**
   * Creates all 1-edge DFS codes supported by a given graph.
   *
   * @param graph graph
   * @return pairs of DFS codes and their embeddings in lexicographical order (min first)
   */
  public DFSCodeEmbeddingsPair[] initSingleEdgeDFSCodes(DMGraph graph) {
    DFSCodeEmbeddingPair[] dfsCodeEmbeddingParis = new DFSCodeEmbeddingPair[graph.getEdgeCount()];

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

      dfsCodeEmbeddingParis[edgeId] = new DFSCodeEmbeddingPair(dfsCode, embedding);
    }

    return sortAndAggregate(dfsCodeEmbeddingParis);
  }

  /**
   * grows all children of all embeddings of a given parent DFS code in a single graph
   *
   * @param graph graph
   * @param dfsCode DFS Code
   * @param embeddings embeddings
   * @return pairs of DFS codes and their embeddings
   */
  public DFSCodeEmbeddingsPair[] growChildDFSCodes(
    DMGraph graph, DFSCode dfsCode, DFSEmbedding[] embeddings) {

    // grow all children by traversing outgoing edges
    DFSCodeEmbeddingPair[] outgoingChildren =
      outgoingGrower.growChildDFSCodes(graph, dfsCode, embeddings);

    // grow all children by traversing incoming edges
    DFSCodeEmbeddingPair[] incomingChildren =
      incomingGrower.growChildDFSCodes(graph, dfsCode, embeddings);

    // union children, sort and sortAndAggregate to a single entry per child DFS Code
    DFSCodeEmbeddingPair[] allChildren = ArrayUtils.addAll(outgoingChildren, incomingChildren);
    return sortAndAggregate(allChildren);
  }

  /**
   * Sorts reported DFS codes by lexicographical order and creates a single output pair for each
   * distinct DFS code including all of its embeddings.
   *
   * @param reports DFS code - embedding - pairs
   * @return DFS code - akk embeddings - pairs in lexicographical order
   */
  public DFSCodeEmbeddingsPair[] sortAndAggregate(DFSCodeEmbeddingPair[] reports) {

    DFSCodeEmbeddingsPair[] aggregates;

    if (reports.length > 0) {
      Arrays.sort(reports);

      DFSCodeEmbeddingPair report = reports[0];

      DFSCodeEmbeddingsPair aggregate =
        new DFSCodeEmbeddingsPair(report.getDfsCode(), report.getEmbedding());

      aggregates = new DFSCodeEmbeddingsPair[] {aggregate};

      for (int i = 1; i < reports.length; i++) {
        report = reports[i];

        if (aggregate.getDfsCode().equals(report.getDfsCode())) {
          aggregate.add(report.getEmbedding());
        } else {
          aggregate = new DFSCodeEmbeddingsPair(report.getDfsCode(), report.getEmbedding());
          aggregates = ArrayUtils.add(aggregates, aggregate);
        }
      }
    } else {
      aggregates = new DFSCodeEmbeddingsPair[0];
    }

    return aggregates;
  }

  public boolean isMinimal(DFSCode dfsCode) {
    DFSCodeEmbeddingsPair minParentPair = initSingleEdgeDFSCodes(dfsCode)[0];

    boolean minimal = minParentPair.getDfsCode().parentOf(dfsCode);

    while (minimal) {
      DFSCode minDFSCode = minParentPair.getDfsCode();
      DFSEmbedding[] embeddings = minParentPair.getEmbeddings();

      DFSCodeEmbeddingsPair[] children = growChildDFSCodes(dfsCode, minDFSCode, embeddings);

      if (children.length == 0) {
        break;
      } else {
        minParentPair = children[0];
        minimal = minParentPair.getDfsCode().parentOf(dfsCode);
      }
    }

    return minimal;
  }

}
