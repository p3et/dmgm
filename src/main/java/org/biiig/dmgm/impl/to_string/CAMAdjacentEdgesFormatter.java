package org.biiig.dmgm.impl.to_string;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.model.collection.LabelDictionary;
import org.biiig.dmgm.api.model.graph.DMGraph;

import java.util.Arrays;
import java.util.Map;

public abstract class CAMAdjacentEdgesFormatter {
  private static final char EDGE_LABEL_SEPARATOR = '|';
  protected static final char OUTGOING = '>';
  protected static final char INCOMING = '<';
  protected static final char EDGE_START_END = '-';
  protected final LabelDictionary vertexDictionary;
  protected final LabelDictionary edgeDictionary;

  public CAMAdjacentEdgesFormatter(LabelDictionary vertexDictionary,
    LabelDictionary edgeDictionary) {
    this.vertexDictionary = vertexDictionary;
    this.edgeDictionary = edgeDictionary;
  }

  public String format(DMGraph graph, int vertexId) {
    // determine outgoing edge labels
    Map<Integer, String[]> edgeLabels = Maps.newHashMap();

    for (int edgeId : getEdgeIds(graph, vertexId)) {
      int adjacentVertexId = getAdjacentVertexId(graph, edgeId);
      String edgeLabel = edgeDictionary.translate(graph.getEdgeLabel(edgeId));

      String[] parallelEdgeLabels = edgeLabels.get(adjacentVertexId);

      parallelEdgeLabels = parallelEdgeLabels == null ?
        new String[] {edgeLabel} :
        ArrayUtils.add(parallelEdgeLabels, edgeLabel);

      edgeLabels.put(adjacentVertexId, parallelEdgeLabels);
    }

    // create outgoing edge strings

    String[] adjacencyStrings = new String[edgeLabels.size()];
    int vertexNumber = 0;

    for (Map.Entry<Integer, String[]> adjacencyEntry : edgeLabels.entrySet()) {
      int adjacentVertexId = adjacencyEntry.getKey();
      String[] parallelEdgeLabels = adjacencyEntry.getValue();

      Arrays.sort(parallelEdgeLabels);

      String edgeLabelsString = StringUtils.join(parallelEdgeLabels, EDGE_LABEL_SEPARATOR);

      adjacencyStrings[vertexNumber] = formatEdge(edgeLabelsString) +
        vertexDictionary.translate(graph.getVertexLabel(adjacentVertexId));

      vertexNumber++;
    }

    Arrays.sort(adjacencyStrings);
    return StringUtils.join(adjacencyStrings);
  }

  protected abstract String formatEdge(String edgeLabelsString);

  protected abstract int getAdjacentVertexId(DMGraph graph, int edgeId);

  protected abstract int[] getEdgeIds(DMGraph graph, int vertexId);
}
