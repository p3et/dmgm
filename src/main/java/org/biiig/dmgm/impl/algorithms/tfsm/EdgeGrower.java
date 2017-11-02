package org.biiig.dmgm.impl.algorithms.tfsm;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.impl.model.graph.DFSCode;

import java.util.List;

public abstract class EdgeGrower implements PatternGrower {
  @Override
  public List<DFSCodeEmbeddingPair> growChildren(
    DMGraph graph, DFSCode parentCode, DFSEmbedding[] parentEmbeddings) {

    List<DFSCodeEmbeddingPair> children = Lists.newLinkedList();

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

              children.add(new DFSCodeEmbeddingPair(childCode, childEmbedding));

              // grow backwards from to
            } else if (toTime < 0) {
              DFSCode childCode = parentCode.growChild(
                fromTime,
                toTime,
                isOutgoing(),
                graph.getEdgeLabel(edgeId),
                graph.getVertexLabel(toId)
              );

              DFSEmbedding childEmbedding = parentEmbedding.expandByEdgeIdAndVertexId(edgeId, toId);

              children.add(new DFSCodeEmbeddingPair(childCode, childEmbedding));
            }
          }
        }

        rightmost = false;
      }
    }

    return children;
  }

  protected abstract int[] getEdgeIds(DMGraph graph, int fromId);

  protected abstract int getToId(DMGraph graph, int edgeId);

  protected abstract boolean isOutgoing();
}
