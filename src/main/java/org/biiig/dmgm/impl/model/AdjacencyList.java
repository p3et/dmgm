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

package org.biiig.dmgm.impl.model;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Extends the standard cached graph by indexes mapping vertices to incoming and outgoing edges.
 * These indexes may improve performance for algorithms that include many traversals.
 * However, this representation requires additional memory and initialization time.
 */
public class AdjacencyList extends CachedGraphBase {

  /**
   * Outgoing edge ids of all vertices,
   * i.e., those where a vertex is the source.
   * Outer array index corresponds to vertex ids.
   */
  private final int[][] outgoingEdgeIds;

  /**
   * Incoming edge ids of all vertices,
   * i.e., those where a vertex is the target.
   * Outer array index corresponds to vertex ids.
   */
  private final int[][] incomingEdgeIds;

  /**
   * Constructor.
   *
   * @param id global graph id
   * @param label graph label
   * @param vertexLabels vertex labels
   * @param edgeLabels edge labels
   * @param sourceIds source ids of edges
   * @param targetIds target ids of edges
   */
  public AdjacencyList(
      long id, int label, int[] vertexLabels, int[] edgeLabels, int[] sourceIds, int[] targetIds) {

    super(id, label, vertexLabels, edgeLabels, sourceIds, targetIds);

    int vertexCount = getVertexCount();
    outgoingEdgeIds = new int[vertexCount][];
    incomingEdgeIds = new int[vertexCount][];

    for (int vertexId = 0; vertexId < getVertexCount(); vertexId++) {
      outgoingEdgeIds[vertexId] = super.getOutgoingEdgeIds(vertexId);
      incomingEdgeIds[vertexId] = super.getIncomingEdgeIds(vertexId);
    }
  }

  @Override
  public int[] getOutgoingEdgeIds(int vertexId) {
    return outgoingEdgeIds[vertexId];
  }

  @Override
  public int[] getIncomingEdgeIds(int vertexId) {
    return incomingEdgeIds[vertexId];
  }

  @Override
  public String toString() {
    return super.toString()
        + ArrayUtils.toString(outgoingEdgeIds)
        + ArrayUtils.toString(incomingEdgeIds);
  }
}
