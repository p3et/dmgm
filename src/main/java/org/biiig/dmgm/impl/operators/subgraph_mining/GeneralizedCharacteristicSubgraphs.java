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

package org.biiig.dmgm.impl.operators.subgraph_mining;

import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.impl.operators.subgraph_mining.characteristic.CategorySupportMethods;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.PropertyKeys;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SupportMethods;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.SpecializableCachedGraph;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GeneralizedCharacteristicSubgraphs
  extends GeneralizedSubgraphsBase<Map<Integer, Long>> {

  private static final String DEFAULT_CATEGORY = "_default";

  private final int defaultCategory;
  private final int categoryKey;


  public GeneralizedCharacteristicSubgraphs(PropertyGraphDB database, float minSupportRel, int maxEdgeCount) {
    super(maxEdgeCount, database, minSupportRel);
    defaultCategory = database.encode(DEFAULT_CATEGORY);
    categoryKey = database.encode(PropertyKeys.CATEGORY);
  }

  @Override
  public SupportMethods getAggregateAndFilter(Map<Long, SpecializableCachedGraph> input) {
    Map<Long, int[]> graphCategories = getGraphCategories(input);
    Map<Integer, Long> categoryMinSupport = getCategoryMinSupport(graphCategories);
    return new CategorySupportMethods(database, parallel, graphCategories, categoryMinSupport, categoryKey);
  }

  private Map<Integer, Long> getCategoryMinSupport(Map<Long, int[]> graphCategories) {
    Map<Integer, Long> categoryCounts = graphCategories
      .values()
      .stream()
      .flatMapToInt(IntStream::of)
      .boxed()
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    return categoryCounts
      .entrySet()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey, e -> (long) Math.round(e.getValue() * minSupportRel)));
  }

  private Map<Long, int[]> getGraphCategories(Map<Long, SpecializableCachedGraph> input) {
    return input
      .values()
      .stream()
      .collect(Collectors.toMap(
        CachedGraph::getId,
        g -> {
          String categoryString = database.getString(g.getId(), categoryKey);
          int category = categoryString == null ? defaultCategory : database.encode(categoryString);
          return new int[]{category};
        }));
  }

}
