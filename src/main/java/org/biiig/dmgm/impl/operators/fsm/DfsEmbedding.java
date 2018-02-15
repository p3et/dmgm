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

package org.biiig.dmgm.impl.operators.fsm;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

/**
 * A mapping between a DFS code and a graph.
 */
class DfsEmbedding implements WithEmbedding {
  // TODO: use one general constructor and extension methods just as in {@code DfsCode}.

  /**
   * The graph's id.
   */
  private final long graphId;
  /**
   * Mapping from vertex discovery time (index) to vertex id (value).
   */
  private final int[] vertexIds;
  /**
   * Mapping from edge discovery time (index) to edge id (value).
   */
  private final int[] edgeIds;

  /**
   * Single-edge Constructor.
   *
   * @param graphId graph id
   * @param fromId traversal start vertex id
   * @param edgeId edge id
   * @param toId traversal end vertex id
   */
  public DfsEmbedding(long graphId, int fromId, int edgeId, int toId) {
    this.graphId = graphId;
    this.vertexIds = fromId == toId ? new int[] {fromId} : new int[] {fromId, toId};
    this.edgeIds = new int[] {edgeId};
  }

  /**
   * Extension constructor.
   *
   * @param graphId graph id
   * @param vertexIds mapped vertex ids
   * @param edgeIds mapped edge ids
   */
  private DfsEmbedding(long graphId, int[] vertexIds, int[] edgeIds) {
    this.vertexIds = vertexIds;
    this.edgeIds = edgeIds;
    this.graphId = graphId;
  }

  /**
   * Extend the embedding by forwards growth,
   * i.e., by an edge that leads to a previously undiscovered vertex.
   *
   * @param edgeId edge id
   * @param vertexId new vertex id
   * @return child embedding
   */
  public DfsEmbedding growForwards(int edgeId, int vertexId) {
    return new DfsEmbedding(
        graphId, ArrayUtils.add(vertexIds, vertexId), ArrayUtils.add(edgeIds, edgeId));
  }

  /**
   * Extend the embedding by backwards growth,
   * i.e., by an edge that leads to a vertex that has been visited before (cycle closing).
   *
   * @param edgeId edge id
   * @return child embedding
   */
  public DfsEmbedding growBackwards(int edgeId) {
    return new DfsEmbedding(graphId, vertexIds.clone(), ArrayUtils.add(edgeIds, edgeId));
  }

  /**
   * Get the vertex id mapped to a specific initial discovery time.
   *
   * @param time discovery time
   * @return vertex id
   */
  public int getVertexId(int time) {
    return vertexIds[time];
  }

  /**
   * Get the initial discovery time mapped to a vertex id.
   *
   * @param vertexId vertex id
   * @return contained ? time >= 0 : -1
   */
  public int getVertexTime(int vertexId) {
    return ArrayUtils.indexOf(vertexIds, vertexId);
  }

  /**
   * Check, if an embedding contains an edge id.
   *
   * @param edgeId edge id
   * @return true <=> edge part of the embedding
   */
  public boolean containsEdgeId(int edgeId) {
    return ArrayUtils.contains(edgeIds, edgeId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DfsEmbedding that = (DfsEmbedding) o;


    boolean equal = that.getVertexCount() == that.getVertexCount();

    if (equal) {
      equal = this.getEdgeCount() == that.getEdgeCount();

      if (equal) {
        for (int vertexId : this.vertexIds) {
          equal = ArrayUtils.contains(that.vertexIds, vertexId);
          if (!equal) {
            break;
          }
        }

        if (equal) {
          for (int vertexId : that.vertexIds) {
            equal = ArrayUtils.contains(this.vertexIds, vertexId);
            if (!equal) {
              break;
            }
          }

          if (equal) {
            for (int vertexId : this.edgeIds) {
              equal = ArrayUtils.contains(that.edgeIds, vertexId);
              if (!equal) {
                break;
              }
            }

            if (equal) {
              for (int vertexId : that.edgeIds) {
                equal = ArrayUtils.contains(this.edgeIds, vertexId);
                if (!equal) {
                  break;
                }
              }
            }
          }
        }
      }
    }

    return equal;
  }

  @Override
  public int hashCode() {
    int result = 1;

    for (int id : vertexIds) {
      result *= id;
    }

    for (int id : edgeIds) {
      result *= id;
    }
    return result;
  }


  @Override
  public String toString() {
    return Arrays.toString(vertexIds) + Arrays.toString(edgeIds);
  }

  @Override
  public DfsEmbedding getEmbedding() {
    return this;
  }

  // GETTER

  public long getGraphId() {
    return graphId;
  }

  public int getVertexCount() {
    return vertexIds.length;
  }

  private int getEdgeCount() {
    return this.edgeIds.length;
  }
}
