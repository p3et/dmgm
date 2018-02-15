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

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * Created by peet on 13.07.17.
 */
public class DFSEmbedding implements WithDFSEmbedding {

  private final long graphId;
  private final int[] vertexIds;
  private final int[] edgeIds;

  public DFSEmbedding(long graphId, int fromId, int edgeId, int toId) {
    this.graphId = graphId;
    this.vertexIds = fromId == toId ? new int[] {fromId} : new int[] {fromId, toId};
    this.edgeIds = new int[] {edgeId};
  }

  public DFSEmbedding(long graphId, int[] vertexIds, int[] edgeIds) {
    this.vertexIds = vertexIds;
    this.edgeIds = edgeIds;
    this.graphId = graphId;
  }

  @Override
  public String toString() {
    return Arrays.toString(vertexIds) + Arrays.toString(edgeIds);
  }

  public int getVertexId(int time) {
    return vertexIds[time];
  }

  public boolean containsEdgeId(int edgeId) {
    return ArrayUtils.contains(edgeIds, edgeId);
  }

  public int getVertexTime(int toId) {
    return ArrayUtils.indexOf(vertexIds, toId);
  }

  public DFSEmbedding expandByEdgeId(int edgeId) {
    return new DFSEmbedding(graphId, vertexIds.clone(), ArrayUtils.add(edgeIds, edgeId));
  }

  public int getVertexCount() {
    return vertexIds.length;
  }

  public DFSEmbedding expandByEdgeIdAndVertexId(int edgeId, int vertexId) {
    return new DFSEmbedding(graphId, ArrayUtils.add(vertexIds, vertexId), ArrayUtils.add(edgeIds, edgeId));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DFSEmbedding that = (DFSEmbedding) o;


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

  private int getEdgeCount() {
    return this.edgeIds.length;
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
  public DFSEmbedding getEmbedding() {
    return this;
  }

  public long getGraphId() {
    return graphId;
  }
}
