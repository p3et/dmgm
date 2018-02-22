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

import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.api.model.GraphView;

public class CAMOutgoingEdgesFormatter extends CAMAdjacentEdgesFormatter {

  public CAMOutgoingEdgesFormatter(PropertyGraphDb db) {
    super(db);
  }

  @Override
  protected String formatEdge(String edgeLabelsString) {
    return EDGE_START_END + edgeLabelsString + OUTGOING;
  }

  @Override
  protected int getAdjacentVertexId(GraphView graph, int edgeId) {
    return graph.getTargetId(edgeId);
  }

  @Override
  protected int[] getEdgeIds(GraphView graph, int vertexId) {
    return graph.getOutgoingEdgeIds(vertexId);
  }
}
