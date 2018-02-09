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

public class SourceIdTargetId {
  private final long sourceId;
  private final long right;

  public SourceIdTargetId(long sourceId, long targetId) {
    this.sourceId = sourceId;
    this.right = targetId;
  }

  public long getSourceId() {
    return sourceId;
  }

  public long getRight() {
    return right;
  }

  @Override
  public int hashCode() {
    return (int)(sourceId ^ (sourceId >>> 32)) + 31 * (int)(right ^ (right >>> 32));
  }

  @Override
  public boolean equals(Object obj) {
    boolean equal;
    if (obj != null && obj.getClass().equals(this.getClass())) {
      SourceIdTargetId that = (SourceIdTargetId) obj;
      equal = this.sourceId == that.sourceId && this.right == that.right;
    } else {
      equal = false;
    }

    return equal;
  }

  @Override
  public String toString() {
    return "(" + sourceId + "," + right + ")";
  }
}
