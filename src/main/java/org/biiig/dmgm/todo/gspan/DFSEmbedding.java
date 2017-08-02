package org.biiig.dmgm.todo.gspan;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * Created by peet on 13.07.17.
 */
public class DFSEmbedding {
  private final int[] vertexIds;
  private final int[] edgeIds;

  public DFSEmbedding(int fromId, int edgeId, int toId) {
    this.vertexIds = fromId == toId ? new int[] {fromId} : new int[] {fromId, toId};
    this.edgeIds = new int[] {edgeId};
  }

  public DFSEmbedding(int[] vertexIds, int[] edgeIds) {
    this.vertexIds = vertexIds;
    this.edgeIds = edgeIds;
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
    return new DFSEmbedding(vertexIds.clone(), ArrayUtils.add(edgeIds, edgeId));
  }

  public int getVertexCount() {
    return vertexIds.length;
  }

  public DFSEmbedding expandByEdgeIdAndVertexId(int edgeId, int vertexId) {
    return new DFSEmbedding(ArrayUtils.add(vertexIds, vertexId), ArrayUtils.add(edgeIds, edgeId));
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

  public int[] getVertexIds() {
    return vertexIds;
  }
}
