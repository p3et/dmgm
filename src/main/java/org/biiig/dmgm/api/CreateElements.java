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

package org.biiig.dmgm.api;

/**
 * Create vertices, edges, graphs and graph collections.
 */
public interface CreateElements extends SymbolDictionary {
  /**
   * Create a vertex.
   *
   * @param label label
   * @return id
   */
  long createVertex(int label);

  /**
   * Create an edge.
   *
   * @param label label
   * @param sourceId source element id
   * @param targetId target element id
   * @return id
   */
  long createEdge(int label, long sourceId, long targetId);

  /**
   * Create a graph (Hypervertex).
   *
   * @param label label
   * @param vertexIds element ids in the role of vertices
   * @param edgeIds element ids in the role of edges
   * @return id
   */
  long createGraph(int label, long[] vertexIds, long[] edgeIds);

  /**
   * Create a graph collection (Hypervertex without edges).
   *
   * @param label label
   * @param graphIds graph ids
   * @return id
   */
  default long createCollection(int label, long[] graphIds) {
    return createGraph(label, graphIds, new long[0]);
  }

  /**
   * Create a vertex.
   *
   * @param label label
   * @return id
   */
  default long createVertex(String label){
    return createVertex(encode(label));
  }

  /**
   * Create an edge.
   *
   * @param label label
   * @param sourceId source element id
   * @param targetId target element id
   * @return id
   */
  default long createEdge(String label, long sourceId, long targetId){
    return createEdge(encode(label), sourceId, targetId);
  }

  /**
   * Create a graph (Hypervertex).
   *
   * @param label label
   * @param vertexIds element ids in the role of vertices
   * @param edgeIds element ids in the role of edges
   * @return id
   */
  default long createGraph(String label, long[] vertexIds, long[] edgeIds){
    return createGraph(encode(label), vertexIds, edgeIds);
  }

  /**
   * Create a graph collection (Hypervertex without edges).
   *
   * @param label label
   * @param graphIds graph ids
   * @return id
   */
  default long createCollection(String label, long[] graphIds) {
    return createCollection(encode(label), graphIds);
  }

}
