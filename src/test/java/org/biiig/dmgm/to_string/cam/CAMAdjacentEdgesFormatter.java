/*
 * This file is part of Directed Multigraph Miner (DMGM).
 *
 * DMGM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DMGM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DMGM. If not, see <http://www.gnu.org/licenses/>.
 */

package org.biiig.dmgm.to_string.cam;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.model.CachedGraph;

import java.util.Arrays;
import java.util.Map;

public abstract class CAMAdjacentEdgesFormatter {
  private static final char EDGE_LABEL_SEPARATOR = '|';
  protected static final char OUTGOING = '>';
  protected static final char INCOMING = '<';
  protected static final char EDGE_START_END = '-';
  protected final PropertyGraphDB db;

  public CAMAdjacentEdgesFormatter(PropertyGraphDB db) {
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
