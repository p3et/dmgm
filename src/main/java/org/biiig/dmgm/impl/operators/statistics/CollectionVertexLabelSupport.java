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

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.config.DmgmConstants;
import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.api.db.VertexIdsEdgeIds;
import org.biiig.dmgm.api.operators.StatisticsExtractor;
import org.biiig.dmgm.impl.operators.common.DmgmOperatorBase;
import org.biiig.dmgm.impl.util.collectors.GroupByKeySumLong;

/**
 * Extract a map: vertex label -> support.
 */
public class CollectionVertexLabelSupport extends DmgmOperatorBase
    implements StatisticsExtractor<Map<Integer, Long>> {

  /**
   * Flag to enable the inclusion of generalized vertex labels.
   * true <=> generalize vertex labels
   */
  private final boolean generalized;

  /**
   * Constructor.
   *
   * @param database database
   * @param parallel true <=> parallel extraction
   * @param generalized true <=> generalize vertex labels
   */
  CollectionVertexLabelSupport(
      PropertyGraphDb database, boolean parallel, boolean generalized) {

    super(database, parallel);
    this.generalized = generalized;
  }

  @Override
  public Map<Integer, Long> apply(Long elementId) {
    long[] graphIds = database.getGraphIdsOfCollection(elementId);

    LongStream stream = getParallelizableLongStream(graphIds);

    Map<Integer, Long> support = stream
        .mapToObj(database::getVertexIdsEdgeIds)
        .filter(Objects::nonNull)
        .map(VertexIdsEdgeIds::getVertexIds)
        .flatMapToInt(this::getVertexLabels)
        .boxed()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    if (generalized) {
      support = support
          .entrySet()
          .stream()
          .flatMap(this::generalize)
          .collect(new GroupByKeySumLong<>(Map.Entry::getKey, Map.Entry::getValue));
    }

    return support;
  }

  /**
   * Get all generalizations, if the label contains the taxonomy path separator.
   *
   * @param bottomLevel input for generalization
   * @return stream of (bottomLevel, support) and all possible (generalizations, support increment)
   */
  private Stream<Map.Entry<Integer, Long>> generalize(Map.Entry<Integer, Long> bottomLevel) {
    String decoded = database.decode(bottomLevel.getKey());
    Collection<Map.Entry<Integer, Long>> output = null;

    if (decoded.contains(DmgmConstants.Separators.TAXONOMY_PATH_LEVEL)) {
      Long support = bottomLevel.getValue();
      output = Lists.newArrayList(bottomLevel);

      while (decoded.contains(DmgmConstants.Separators.TAXONOMY_PATH_LEVEL)) {
        decoded = StringUtils
            .substringBeforeLast(decoded, DmgmConstants.Separators.TAXONOMY_PATH_LEVEL);
        Integer encoded = database.encode(decoded);
        output.add(new HashMap.SimpleEntry<>(encoded, support));
      }
    }

    return output == null ? Stream.of(bottomLevel) : output.stream();
  }

  /**
   * To determine support, return only distinct labels of each graph.
   *
   * @param vertexIds vertex ids of a single graph
   * @return distinct vertex labels
   */
  private IntStream getVertexLabels(long[] vertexIds) {
    return LongStream.of(vertexIds)
        .mapToInt(database::getLabel)
        .distinct();
  }

}
