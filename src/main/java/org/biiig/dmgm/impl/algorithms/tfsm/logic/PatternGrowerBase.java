package org.biiig.dmgm.impl.algorithms.tfsm.logic;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.model.graph.IntGraph;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSCodeEmbeddingPair;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSEmbedding;
import org.biiig.dmgm.impl.model.graph.DFSCode;

public abstract class PatternGrowerBase implements PatternGrower {

  @Override
  public DFSCodeEmbeddingPair[] growChildDFSCodes(
    IntGraph graph, DFSCode parentCode, DFSEmbedding[] parentEmbeddings) {

    DFSCodeEmbeddingPair[] children = new DFSCodeEmbeddingPair[0];

    for (DFSEmbedding parentEmbedding : parentEmbeddings) {
      boolean rightmost = true;
      for (int fromTime : parentCode.getRightmostPath()) {
        int fromId = parentEmbedding.getVertexId(fromTime);

        for (int edgeId : getEdgeIds(graph, fromId)) {
          // if not contained in parent embedding
          if (! parentEmbedding.containsEdgeId(edgeId)) {

            // determine times of incident vertices in parent embedding
            int toId = getToId(graph, edgeId);
            int toTime = parentEmbedding.getVertexTime(toId);

            // CHECK FOR BACKWARDS GROWTH OPTIONS

            // grow backwards
            if (rightmost && toTime >= 0) {
              DFSCode childCode = parentCode.growChild(
                fromTime,
                toTime,
                isOutgoing(),
                graph.getEdgeLabel(edgeId),
                graph.getVertexLabel(toId)
              );

              DFSEmbedding childEmbedding = parentEmbedding.expandByEdgeId(edgeId);

              children =
                ArrayUtils.add(children, new DFSCodeEmbeddingPair(childCode, childEmbedding));

              // grow backwards from to
            } else if (toTime < 0) {
              toTime = parentCode.getVertexCount();

              DFSCode childCode = parentCode.growChild(
                fromTime,
                toTime,
                isOutgoing(),
                graph.getEdgeLabel(edgeId),
                graph.getVertexLabel(toId)
              );

              DFSEmbedding childEmbedding = parentEmbedding.expandByEdgeIdAndVertexId(edgeId, toId);

              children =
                ArrayUtils.add(children, new DFSCodeEmbeddingPair(childCode, childEmbedding));            }
          }
        }

        rightmost = false;
      }
    }

    return children;
  }

  protected abstract int[] getEdgeIds(IntGraph graph, int fromId);

  protected abstract int getToId(IntGraph graph, int edgeId);

  protected abstract boolean isOutgoing();
}
