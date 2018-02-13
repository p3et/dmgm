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

import org.biiig.dmgm.api.config.DMGMConstants;
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.PropertyKeys;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SupportSpecialization;

public abstract class SupportSpecializationBase<G extends CachedGraph, S> implements SupportSpecialization<S> {

  protected final boolean parallel;
  protected final S minSupportAbsolute;
  protected final PropertyGraphDB db;
  protected final int dfsCodeKey;
  protected final int supportKey;

  public SupportSpecializationBase(PropertyGraphDB db, S minSupportAbsolute, boolean parallel) {
    this.parallel = parallel;
    this.minSupportAbsolute = minSupportAbsolute;
    this.db = db;
    this.supportKey = db.encode(PropertyKeys.SUPPORT);
    this.dfsCodeKey = db.encode(DMGMConstants.PropertyKeys.DFS_CODE);
  }

  protected long createGraph(PropertyGraphDB db, CachedGraph graph) {
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
