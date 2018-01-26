package org.biiig.dmgm.impl.graph;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.api.LabelDictionary;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

public class SmallGraphBase implements SmallGraph {
  private final long id;
  private final int label;
  protected  final int[] vertexLabels;
  protected final int[] edgeLabels;
  protected final int[] sourceIds;
  protected final int[] targetIds;

  public SmallGraphBase(long id, int label, int[] vertexLabels, int[] edgeLabels, int[] sourceIds, int[] targetIds) {
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
  public void setLabel(int label) {
    this.label = label;
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
  public int addVertex(int label) {
    vertexLabels = ArrayUtils.add(vertexLabels, label);
    return vertexLabels.length - 1;
  }

  @Override
  public int addEdge(int sourceId, int targetId, int label) {
    edgeLabels = ArrayUtils.add(edgeLabels, label);
    sourceIds = ArrayUtils.add(sourceIds, sourceId);
    targetIds = ArrayUtils.add(targetIds, targetId);
    return edgeLabels.length - 1;
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
    int[] edgeIds = new int[0];

    for (int edgeId = 0; edgeId < getEdgeCount(); edgeId++)
      if (sourceIds[edgeId] == vertexId)
        edgeIds = ArrayUtils.add(edgeIds, edgeId);

    return edgeIds;
  }

  @Override
  public int[] getIncomingEdgeIds(int vertexId) {
    int[] edgeIds = new int[0];

    for (int edgeId = 0; edgeId < getEdgeCount(); edgeId++)
      if (targetIds[edgeId] == vertexId)
        edgeIds = ArrayUtils.add(edgeIds, edgeId);

    return edgeIds;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
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
  public void setVertexLabel(int id, int label) {
    vertexLabels[id] = label;
  }

  @Override
  public String toString(LabelDictionary dictionary) {
    return toString(dictionary::translate);
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

    return "g=" +
      "\nV=" + Arrays.toString(formattedVertexLabels) +
      "\nE=" + Arrays.toString(formattedEdgeLabels) +
      "\nS=" + Arrays.toString(sourceIds) +
      "\nT=" + Arrays.toString(targetIds);
  }

}
