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

package org.biiig.dmgm.impl.graph;

import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.db.CachedGraph;

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
  public String toString(PropertyGraphDB db) {
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
