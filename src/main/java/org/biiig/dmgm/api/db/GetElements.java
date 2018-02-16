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

package org.biiig.dmgm.api.db;

/**
 * Read vertices, edges, graphs and model collections.
 */
public interface GetElements {
  /**
   * Get the label of an element.
   *
   * @param id model, vertex or edge id
   * @return label
   */
  int getLabel(long id);

  /**
   * Get source and target of an edge.
   *
   * @param edgeId edge id
   * @return (sourceId, targetId)
   */
  SourceIdTargetId getSourceIdTargetId(long edgeId);

  /**
   * Get vertices and edges of a model.
   *
   * @param graphId graph id
   * @return (vertexId..., edgeId...)
   */
  VertexIdsEdgeIds getVertexIdsEdgeIds(long graphId);

  /**
   * get graph of a collection.
   * @param collectionId collection id
   * @return graphId...
   */
  long[] getGraphIdsOfCollection(Long collectionId);

  /**
   * Get all ids of elements that appear in the role of a model,
   * i.e., have associated vertices and edges.
   *
   * @return element ids
   */
  long[] getGraphIds();

  /**
   * Get all model ids in which a vertex appears.
   *
   * @param vertexId vertex id
   * @return model ids
   */
  long[] getGraphIdsOfVertex(long vertexId);

  /**
   * Get all edge ids in which a vertex appears.
   *
   * @param edgeId edge id
   * @return model ids
   */
  long[] getGraphIdsOfEdge(long edgeId);

  /**
   * Get all ids of elements that appear at least once in the role of a vertex,
   * i.e., are part of the vertex set of at least one model.
   *
   * @return element ids
   */
  long[] getVertexIds();

  /**
   * Get all ids of elements that appear at least once in the role of an edge,
   * i.e., have an associated source and target vertex.
   *
   * @return element ids
   */
  long[] getEdgeIds();

  /**
   * Get all ids of elements that appear in the role of a model collection,
   * i.e., have associated vertices (graphs) but no edges.
   *
   * @return element ids
   */
  long[] getCollectionIds();
}
