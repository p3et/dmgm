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

import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.api.model.GraphView;

import java.util.function.Function;

public class ELGraphFormatter implements Function<GraphView, String> {
  private static final char EDGE_SEPARATOR = ',';

  private final ELTripleFormatter edgeFormatter;

  public ELGraphFormatter(PropertyGraphDb db) {
    this.edgeFormatter = new ELTripleFormatter(db);
  }

  @Override
  public String apply(GraphView graph) {
    String[] edgeStrings = new String[graph.getEdgeCount()];

    for (int edgeId = 0; edgeId < graph.getEdgeCount(); edgeId++) {
      edgeStrings[edgeId] = edgeFormatter.format(graph, edgeId);
    }

    return StringUtils.join(edgeStrings, EDGE_SEPARATOR);
  }
}
