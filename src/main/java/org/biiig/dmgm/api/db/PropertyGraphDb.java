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

import java.util.List;

import org.biiig.dmgm.api.model.CachedGraph;

/**
 * Describes a database that supports:
 * - the Extended Property Graph Model @see <a href="https://dl.acm.org/citation.cfm?id=2980527">EPGM</a>
 * - edges between arbitrary elements (vertices, graphs, edges)
 * - dictionary coding for all symbols such as labels and property keys
 * .
 */
public interface PropertyGraphDb extends
    SymbolDictionary, CreateElements, GetElements, SetProperties, GetProperties, QueryElements {

  /**
   * Materialize a single model and return a Pojo representation.
   *
   * @param graphId model id
   * @return cached immutable model pojo
   */
  CachedGraph getCachedGraph(long graphId);

  /**
   * Materialize a single model and return a List of it's graphs pojo representations.
   *
   * @param collectionId hypervertex id
   * @return list of cached immutable model pojos
   */
  List<CachedGraph> getCachedCollection(long collectionId);

}
