package org.biiig.dmgm.impl.graph;

import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.CachedGraph;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

public class CachedGraphBase implements CachedGraph {
  private final long id;
  protected final int label;
  protected  final int[] vertexLabels;
  protected final int[] edgeLabels;
  protected final int[] sourceIds;
  protected final int[] targetIds;

  public CachedGraphBase(long id, int label, int[] vertexLabels, int[] edgeLabels, int[] sourceIds, int[] targetIds) {
    this.id = id;
    this.label = label;
    this.vertexLabels = vertexLabels;
    this.edgeLabels = edgeLabels;
    this.sourceIds = sourceIds;
    this.targetIds = targetIds;
  }


  @Override
  public int getLabel() {
    return label;
  }

  @Override
  public int getVertexCount() {
    return vertexLabels.length;
  }

  @Override
  public int getEdgeCount() {
    return edgeLabels.length;
  }

  @Override
  public int getVertexLabel(int vertexId) {
    return vertexLabels[vertexId];
  }

  @Override
  public int getEdgeLabel(int edgeId) {
    return edgeLabels[edgeId];
  }

  @Override
  public int getSourceId(int edgeId) {
    return sourceIds[edgeId];
  }

  @Override
  public int getTargetId(int edgeId) {
    return targetIds[edgeId];
  }

  @Override
  public int[] getOutgoingEdgeIds(int vertexId) {
    return IntStream.range(0, getEdgeCount())
      .filter(edgeId -> sourceIds[edgeId] == vertexId)
      .toArray();
  }

  @Override
  public int[] getIncomingEdgeIds(int vertexId) {
    return IntStream.range(0, getEdgeCount())
      .filter(edgeId -> targetIds[edgeId] == vertexId)
      .toArray();
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public IntStream vertexIdStream() {
    return IntStream.range(0, getVertexCount());
  }

  @Override
  public IntStream edgeIdStream() {
    return IntStream.range(0, getEdgeCount());
  }

  @Override
  public String toString(GraphDB db) {
    return toString(db::decode);
  }

  @Override
  public int[] getVertexLabels() {
    return vertexLabels;
  }

  @Override
  public int[] getEdgeLabels() {
    return edgeLabels;
  }

  @Override
  public void setVertexLabel(int vertexId, int label) {
    this.vertexLabels[vertexId] = label;
  }

  @Override
  public int[] getSourceIds() {
    return sourceIds;
  }

  @Override
  public int[] getTargetIds() {
    return targetIds;
  }

  @Override
  public String toString() {
    return toString(Object::toString);
  }

  private String toString(Function<Integer, String> labelFormatter) {
    String[] formattedVertexLabels = new String[vertexLabels.length];
    for (int i = 0; i < vertexLabels.length; i++)
      formattedVertexLabels[i] = labelFormatter.apply(vertexLabels[i]);

    String[] formattedEdgeLabels = new String[edgeLabels.length];
    for (int i = 0; i < edgeLabels.length; i++)
      formattedEdgeLabels[i] = labelFormatter.apply(edgeLabels[i]);

    return id + ":" + label +
      "\nV=" + Arrays.toString(formattedVertexLabels) +
      "\nE=" + Arrays.toString(formattedEdgeLabels) +
      "\nS=" + Arrays.toString(sourceIds) +
      "\nT=" + Arrays.toString(targetIds);
  }
}
