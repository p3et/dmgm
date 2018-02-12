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

package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import org.biiig.dmgm.api.db.CachedGraph;
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.PropertyKeys;

public class SupportMethodsBase {
  protected static final String DFS_CODE = "_dfsCode";
  protected final PropertyGraphDB database;
  protected final int dfsCodeKey;
  protected final int supportKey;
  protected final boolean parallel;

  public SupportMethodsBase(PropertyGraphDB database, boolean parallel) {
    this.database = database;
    supportKey = database.encode(PropertyKeys.SUPPORT);
    dfsCodeKey = database.encode(DFS_CODE);
    this.parallel = parallel;
  }

  public long createGraph(PropertyGraphDB db, CachedGraph graph) {
    int vertexCount = graph.getVertexCount();
    long[] vertexIds = new long[vertexCount];
    for (int v = 0; v < vertexCount; v++)
      vertexIds[v] = db.createVertex(graph.getVertexLabel(v));


    int edgeCount = graph.getEdgeCount();
    long[] edgeIds = new long[edgeCount];
    for (int e = 0; e < edgeCount; e++) {
      int label = graph.getEdgeLabel(e);
      long sourceId = vertexIds[graph.getSourceId(e)];
      long targetId = vertexIds[graph.getTargetId(e)];
      edgeIds[e] = db.createEdge(label, sourceId, targetId);
    }

    return db.createGraph(graph.getLabel(), vertexIds, edgeIds);
  }
}
