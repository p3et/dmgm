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

package org.biiig.dmgm.api.db;

import java.util.List;

/**
 * Describes a database that supports:
 * - the Extended Property Graph Model
 * - edges between arbitrary elements (vertices, graphs, edges)
 * - dictionary coding for all symbols such as labels and property keys
 */
public interface PropertyGraphDB
  extends SymbolDictionary, CreateElements, GetElements, SetProperties, GetProperties, QueryElements {

  /**
   * Materialize a single graph and return a Pojo representation.
   *
   * @param graphId graph id
   * @return cached immutable graph pojo
   */
  CachedGraph getCachedGraph(long graphId);

  /**
   * Materialize a single graph and return a List of it's graphs pojo representations.
   *
   * @param collectionId hypervertex id
   * @return list of cached immutable graph pojos
   */
  List<CachedGraph> getCachedCollection(long collectionId);

}
