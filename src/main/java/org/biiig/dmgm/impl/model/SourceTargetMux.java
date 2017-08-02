package org.biiig.dmgm.impl.model;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Graph representation without indexes.
 * Source and target of an edge are multiplexed in a integer array, i.e., [s0,t0,s1,t1,..].
 */
public class SourceTargetMux extends DirectedGraphBase {
  private int[] sourceTargetMux;

  SourceTargetMux(int vertexCount, int edgeCount) {
    super(edgeCount, vertexCount);
    sourceTargetMux = new int[edgeCount * 2];
  }

  @Override
  public void setVertex(int vertexId, int[] data) {
    vertexData[vertexId] = data;
  }

  @Override
  public void setEdge(int edgeId, int sourceId, int targetId, int[] data) {
    sourceTargetMux[getSourceMuxIndex(edgeId)] = sourceId;
    sourceTargetMux[getTargetMuxIndex(edgeId)] = targetId;
    edgeData[edgeId] = data;
  }

  @Override
  public int getSourceId(int edgeId) {
    return sourceTargetMux[getSourceMuxIndex(edgeId)];
  }

  @Override
  public int getTargetId(int edgeId) {
    return sourceTargetMux[getTargetMuxIndex(edgeId)];
  }

  @Override
  public int[] getOutgoingEdgeIds(int vertexId) {
    return getIncidentEdgeIds(vertexId, true);
  }

  @Override
  public int[] getIncomingEdgeIds(int vertexId) {
    return getIncidentEdgeIds(vertexId, false);
  }

  private int getSourceMuxIndex(int edgeId) {
    return edgeId * 2;
  }

  private int getTargetMuxIndex(int edgeId) {
    return getSourceMuxIndex(edgeId) + 1;
  }

  private int[] getIncidentEdgeIds(int vertexId, boolean outgoing) {
    int i = 0;
    int edgeCount = getEdgeCount();
    int[] edgeIds = new int[edgeCount];

    for (int edgeId = 0; edgeId < edgeCount; edgeId++) {
      int currentId = outgoing ? getSourceId(edgeId) : getTargetId(edgeId);
      if (currentId == vertexId) {
        edgeIds[i] = edgeId;
        i++;
      }

    }

    if (i != edgeCount) {
      if (i == 0) {
        edgeIds = new int[0];
      } else {
        edgeIds = ArrayUtils.subarray(edgeIds, 0, i);
      }
    }

    return edgeIds;
  }
}
