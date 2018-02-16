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
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.api.db.VertexIdsEdgeIds;
import org.biiig.dmgm.api.operators.StatisticsExtractor;
import org.biiig.dmgm.impl.operators.common.DmgmOperatorBase;

/**
 * Superclass of extractors for label statistics from collections.
 */
abstract class CollectionElementLabelSupportBase extends DmgmOperatorBase
    implements StatisticsExtractor<Map<Integer, Long>> {

  /**
   * Constructor.
   *
   * @param database database
   * @param parallel parallel execution flag
   */
  CollectionElementLabelSupportBase(PropertyGraphDb database, boolean parallel) {
    super(database, parallel);
  }

  /**
   * To determine support, return only distinct labels of each graph.
   *
   * @param elementIds vertex or edge ids of a single graph
   * @return distinct vertex or edge labels
   */
  protected IntStream getDistinctLabels(long[] elementIds) {
    return LongStream.of(elementIds)
        .mapToInt(database::getLabel)
        .distinct();
  }

  /**
   * Get the parallelizable stream of graph element id pairs of a collection.
   *
   * @param graphId collection id
   * @return (vertexId...,edgeId...)...
   */
  protected Stream<VertexIdsEdgeIds> getGraphStream(Long graphId) {
    long[] graphIds = database.getGraphIdsOfCollection(graphId);

    LongStream stream = getParallelizableLongStream(graphIds);

    return stream
        .mapToObj(database::getVertexIdsEdgeIds)
        .filter(Objects::nonNull);
  }
}
