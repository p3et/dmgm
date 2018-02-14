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


package org.biiig.dmgm.impl.operators;

import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.model.CachedGraph;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Superclass of DMGM operators.
 */
public abstract class DMGMOperatorBase implements DMGMOperator {

  /**
   * Flag to enable parallel execution of the operator.
   * true <=> enabled
   */
  protected final boolean parallel;
  /**
   * Database of in- and output elements.
   */
  protected final PropertyGraphDB database;

  /**
   * Constructor.
   *
   * @param parallel parallel execution flag
   * @param database database
   */
  protected DMGMOperatorBase(boolean parallel, PropertyGraphDB database) {
    this.parallel = parallel;
    this.database = database;
  }

  @Override
  public <T> Stream<T> getParallelizableStream(Collection<T> collection) {
    return parallel ?
      collection.parallelStream() :
      collection.stream();
  }

  @Override
  public PropertyGraphDB getDatabase() {
    return database;
  }

  @Override
  public long createGraph(CachedGraph graph) {
    int vertexCount = graph.getVertexCount();
    long[] vertexIds = new long[vertexCount];
    for (int v = 0; v < vertexCount; v++)
      vertexIds[v] = database.createVertex(graph.getVertexLabel(v));


    int edgeCount = graph.getEdgeCount();
    long[] edgeIds = new long[edgeCount];
    for (int e = 0; e < edgeCount; e++) {
      int label = graph.getEdgeLabel(e);
      long sourceId = vertexIds[graph.getSourceId(e)];
      long targetId = vertexIds[graph.getTargetId(e)];
      edgeIds[e] = database.createEdge(label, sourceId, targetId);
    }

    return database.createGraph(graph.getLabel(), vertexIds, edgeIds);
  }
}
