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

package org.biiig.dmgm.impl.operators.statistics;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.api.db.VertexIdsEdgeIds;

/**
 * Extract a map: vertex label -> support.
 */
public class CollectionEdgeLabelSupport extends CollectionElementLabelSupportBase {

  /**
   * Constructor.
   *
   * @param database database
   * @param parallel true <=> parallel extraction
   */
  CollectionEdgeLabelSupport(PropertyGraphDb database, boolean parallel) {
    super(database, parallel);
  }

  @Override
  public Map<Integer, Long> apply(Long elementId) {
    Stream<VertexIdsEdgeIds> graphStream = getGraphStream(elementId);

    return graphStream
        .map(VertexIdsEdgeIds::getEdgeIds)
        .flatMapToInt(this::getDistinctLabels)
        .boxed()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
  }
}
