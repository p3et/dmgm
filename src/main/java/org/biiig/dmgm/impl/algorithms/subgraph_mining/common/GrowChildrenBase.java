package org.biiig.dmgm.impl.algorithms.subgraph_mining.common;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.impl.graph.DFSCode;

public abstract class GrowChildrenBase implements GrowChildren {

  @Override
  public DFSCodeEmbeddingPair[] apply(
    Graph graph, DFSCode parentCode, int[] rightmostPath, DFSEmbedding parentEmbedding) {

    DFSCodeEmbeddingPair[] children = new DFSCodeEmbeddingPair[0];

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
            DFSCode childCode = parentCode.deepCopy();
            childCode.addEdge(fromTime, toTime, graph.getEdgeLabel(edgeId), isOutgoing());

            DFSEmbedding childEmbedding = parentEmbedding.expandByEdgeId(edgeId);

            children =
              ArrayUtils.add(children, new DFSCodeEmbeddingPair(childCode, childEmbedding));

            // grow backwards from to
          } else if (toTime < 0) {
            toTime = parentCode.getVertexCount();

            DFSCode childCode = parentCode.deepCopy();
            childCode.addVertex(graph.getVertexLabel(toId));
            childCode.addEdge(fromTime, toTime, graph.getEdgeLabel(edgeId), isOutgoing());

            DFSEmbedding childEmbedding = parentEmbedding.expandByEdgeIdAndVertexId(edgeId, toId);

            children =
              ArrayUtils.add(children, new DFSCodeEmbeddingPair(childCode, childEmbedding));            }
        }
      }

      rightmost = false;
    }

    return children;
  }

  protected abstract int[] getEdgeIds(Graph graph, int fromId);

  protected abstract int getToId(Graph graph, int edgeId);

  protected abstract boolean isOutgoing();
}
