package org.biiig.dmgm.impl.model.graph;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

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
  public void setVertex(int vertexId, int label) {
    setVertex(vertexId, new int[] {label});
  }

  @Override
  public void setVertex(int vertexId, int[] labels) {
    vertexData[vertexId] = labels;
  }

  @Override
  public void setEdge(int edgeId, int sourceId, int targetId, int label) {
    setEdge(edgeId, sourceId, targetId, new int[] {label});
  }

  @Override
  public void setEdge(int edgeId, int sourceId, int targetId, int[] labels) {
    sourceTargetMux[getSourceMuxIndex(edgeId)] = sourceId;
    sourceTargetMux[getTargetMuxIndex(edgeId)] = targetId;
    edgeData[edgeId] = labels;
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

  @Override
  public void trim() {
    for (int v = 0; v < getVertexCount(); v++) {
      if (getVertexData(v) == null) {
        vertexData = ArrayUtils.subarray(vertexData, 0, v);
        break;
      }
    }

    for (int e = 0; e < getEdgeCount(); e++) {
      if (getEdgeData(e) == null) {
        sourceTargetMux = ArrayUtils.subarray(sourceTargetMux, 0, 2 * e);
        edgeData = ArrayUtils.subarray(edgeData, 0, e);
        break;
      }
    }
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

  private int getSourceMuxIndex(int edgeId) {
    return edgeId * 2;
  }

  private int getTargetMuxIndex(int edgeId) {
    return getSourceMuxIndex(edgeId) + 1;
  }

  @Override
  public String toString() {

    List<String> edgeStrings = Lists.newArrayListWithExpectedSize(getEdgeCount());

    for (int i = 0; i < getEdgeCount(); i++) {
      edgeStrings.add(String.valueOf(i) + ":<" + getSourceId(i) + ";" + getTargetId(i) + ">");
    }

    return super.toString() + "\n" +
      "E=" + StringUtils.join(edgeStrings, ",");
  }
}
