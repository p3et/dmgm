package org.biiig.dmgm.impl.to_string.cam;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.CachedGraph;

import java.util.Arrays;
import java.util.Map;

public abstract class CAMAdjacentEdgesFormatter {
  private static final char EDGE_LABEL_SEPARATOR = '|';
  protected static final char OUTGOING = '>';
  protected static final char INCOMING = '<';
  protected static final char EDGE_START_END = '-';
  protected final GraphDB db;

  public CAMAdjacentEdgesFormatter(GraphDB db) {
    this.db = db;
  }

  public String format(CachedGraph graph, int vertexId) {
    // determine outgoing edge labels
    Map<Integer, String[]> edgeLabels = Maps.newHashMap();

    for (int edgeId : getEdgeIds(graph, vertexId)) {
      int adjacentVertexId = getAdjacentVertexId(graph, edgeId);
      String edgeLabel = db.decode(graph.getEdgeLabel(edgeId));

      String[] parallelEdgeLabels = edgeLabels.get(adjacentVertexId);

      parallelEdgeLabels = parallelEdgeLabels == null ?
        new String[] {edgeLabel} :
        ArrayUtils.add(parallelEdgeLabels, edgeLabel);

      edgeLabels.put(adjacentVertexId, parallelEdgeLabels);
    }

    // aggregateReports outgoing edge strings

    String[] adjacencyStrings = new String[edgeLabels.size()];
    int vertexNumber = 0;

    for (Map.Entry<Integer, String[]> adjacencyEntry : edgeLabels.entrySet()) {
      int adjacentVertexId = adjacencyEntry.getKey();
      String[] parallelEdgeLabels = adjacencyEntry.getValue();

      Arrays.sort(parallelEdgeLabels);

      String edgeLabelsString = StringUtils.join(parallelEdgeLabels, EDGE_LABEL_SEPARATOR);

      adjacencyStrings[vertexNumber] = formatEdge(edgeLabelsString) +
        db.decode(graph.getVertexLabel(adjacentVertexId));

      vertexNumber++;
    }

    Arrays.sort(adjacencyStrings);
    return StringUtils.join(adjacencyStrings);
  }

  protected abstract String formatEdge(String edgeLabelsString);

  protected abstract int getAdjacentVertexId(CachedGraph graph, int edgeId);

  protected abstract int[] getEdgeIds(CachedGraph graph, int vertexId);
}
