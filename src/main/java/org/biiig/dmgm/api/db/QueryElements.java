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

import java.util.function.BiFunction;
import java.util.function.IntPredicate;

/**
 * Get vertices, edges, graphs and model collections by data-related queries.
 */
public interface QueryElements {
  /**
   * Query identifiers of elements matching a given label predicate.
   *
   * @param labelPredicate label predicate
   * @return identifiers
   */
  long[] queryElements(IntPredicate labelPredicate);

  /**
   * Query identifiers of elements matching a given property predicate.
   *
   * @param propertyPredicate over elementId and property store
   * @return
   */
  long[] queryElements(PropertyPredicate propertyPredicate);

  /**
   * Query identifiers of elements matching a given label and property predicate.
   *
   * @param propertyPredicate over elementId and property store
   * @return
   */
  long[] queryElements(IntPredicate labelPredicate, PropertyPredicate propertyPredicate);

  /**
   * A predicate that evaluates an element's properties.
   */
  interface PropertyPredicate extends BiFunction<GetProperties, Long, Boolean> {

    /**
     * Evaluate the predicate.
     *
     * @param getProperties property store read access
     * @param elementId id of a model, vertex or edge
     * @return evaluation result
     */
    @Override
    Boolean apply(GetProperties getProperties, Long elementId);
  }
}
