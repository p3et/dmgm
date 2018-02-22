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

package org.biiig.dmgm.to_string.edge_list;

import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.api.model.GraphView;

public class ELVertexFormatter {

  private final PropertyGraphDb vertexDictionary;

  public ELVertexFormatter(PropertyGraphDb db) {
    this.vertexDictionary = db;
  }

  public String format(GraphView graph, int vertexId) {
    return "(" + vertexId + ":" + vertexDictionary.decode(graph.getVertexLabel(vertexId)) + ")";
  }
}
