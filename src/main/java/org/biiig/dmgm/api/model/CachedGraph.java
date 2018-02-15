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

package org.biiig.dmgm.api.model;

import org.biiig.dmgm.api.db.SymbolDictionary;
import org.biiig.dmgm.impl.operators.fsm.WithGraph;

/**
 * A materialized model, i.e., a model including all its vertices and edges.
 * NOTE:  There are global ids (long) and local ids (int).
 *        The latter are only valid in the scope of a model.
 *        Local ids typically correspond to indexes of vertex and edge arrays.
 *        Thus, the structure of cached graphs is immutable.
 *
 */
public interface CachedGraph extends WithGraph {

  @Override
  default CachedGraph getGraph() {
    return this;
  }

  /**
   * Get the model's identifier.
   *
   * @return global model id
   */
  long getId();

  /**
   * Get the model's label.
   *
   * @return label
   */
  int getLabel();

  /**
   * Get the label of a vertex.
   *
   * @param vertexId local vertex id.
   * @return
   */
  int getVertexLabel(int vertexId);

  /**
   * Get the label of an edge.
   *
   * @param edgeId local edge id
   * @return label
   */
  int getEdgeLabel(int edgeId);

  /**
   * Get the local source vertex id of an edge.
   *
   * @param edgeId local edge id
   * @return local source id
   */
  int getSourceId(int edgeId);

  /**
   * Get the local target vertex id of an edge.
   *
   * @param edgeId local edge id
   * @return local target id
   */
  int getTargetId(int edgeId);

  /**
   * Get all outgoing edge ids of a vertex,
   * i.e., those where a vertex is source.
   *
   * @param vertexId local vertex id
   * @return edge ids
   */
  int[] getOutgoingEdgeIds(int vertexId);

  /**
   * Get all incoming edge ids of a vertex,
   * i.e., those where a vertex is target.
   *
   * @param vertexId local vertex id
   * @return edge ids
   */
  int[] getIncomingEdgeIds(int vertexId);

  /**
   * Get the vertex count.
   *
   * @return vertex count
   */
  int getVertexCount();

  /**
   * Get the edge count.
   *
   * @return edge count
   */
  int getEdgeCount();

  /**
   * Get all vertex labels.
   *
   * @return labels
   */
  int[] getVertexLabels();

  /**
   * Get all edge labels.
   *
   * @return labels
   */
  int[] getEdgeLabels();

  /**
   * Get all source vertex ids.
   *
   * @return vertex ids
   */
  int[] getSourceIds();

  /**
   * Get all target vertex ids.
   *
   * @return vertex ids
   */
  int[] getTargetIds();

  /**
   * Get a string representation with decoded labels.
   *
   * @param dictionary label dictionary
   * @return string representation
   */
  String toString(SymbolDictionary dictionary);
}
