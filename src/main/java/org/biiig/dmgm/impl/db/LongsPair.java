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

package org.biiig.dmgm.impl.db;

import org.biiig.dmgm.api.db.VertexIdsEdgeIds;

import java.util.Arrays;

public class LongsPair implements VertexIdsEdgeIds {
  private final long[] vertexIds;
  private final long[] edgeIds;

  public LongsPair(long[] vertexIds, long[] edgeIds) {
    this.vertexIds = vertexIds;
    this.edgeIds = edgeIds;
  }

  @Override
  public long[] getVertexIds() {
    return vertexIds;
  }

  @Override
  public long[] getEdgeIds() {
    return edgeIds;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(vertexIds) + 31 * Arrays.hashCode(edgeIds);
  }

  @Override
  public boolean equals(Object obj) {
    boolean equal;
    if (obj != null && obj.getClass().equals(this.getClass())) {
      LongsPair that = (LongsPair) obj;
      equal = Arrays.equals(this.vertexIds, that.vertexIds) && Arrays.equals(this.edgeIds, that.edgeIds);
    } else {
      equal = false;
    }

    return equal;
  }

  @Override
  public String toString() {
    return "(" + Arrays.toString(vertexIds) + "," + Arrays.toString(edgeIds) + ")";
  }
}
