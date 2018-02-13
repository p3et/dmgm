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
import org.biiig.dmgm.impl.operators.subgraph_mining.characteristic.CategoryFrequentSupportSpecialization;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SupportSpecialization;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface Characteristic<G extends CachedGraph> extends SubgraphMiningVariant<G, Map<Integer, Long>> {


  @Override
  default SupportSpecialization<Map<Integer, Long>> getSupportSpecialization(
    Map<Long, G> indexedGraphs, PropertyGraphDB db, Map<Integer, Long> minSupportAbs, boolean parallel) {
    Map<Long, int[]> graphCategories = getGraphCategories(indexedGraphs);
    return new CategoryFrequentSupportSpecialization<>(db, minSupportAbs, parallel, graphCategories);
  }

  @Override
  default Map<Integer, Long> getMinSupportAbsolute(Map<Long, G> input, float minSupportRel) {
    Map<Integer, Long> categoryCounts = getGraphCategories(input)
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

  default Map<Long, int[]> getGraphCategories(Map<Long, G> input) {
    return input
      .values()
      .stream()
      .collect(Collectors.toMap(
        CachedGraph::getId,
        g -> {
          PropertyGraphDB db = getDB();
          int categoryKey = getCategoryKey();
          String categoryString = db.getString(g.getId(), categoryKey);
          int defaultCategory = getDefaultCategory();
          int category = categoryString == null ? defaultCategory : db.encode(categoryString);
          return new int[]{category};
        }));
  }

  int getDefaultCategory();

  int getCategoryKey();

  PropertyGraphDB getDB();

}
