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

package org.biiig.dmgm.impl.operators.fsm;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javafx.util.Pair;
import org.biiig.dmgm.api.config.DmgmConstants;
import org.biiig.dmgm.api.db.GetProperties;
import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.api.db.SetProperties;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.api.operators.DmgmOperator;

/**
 * Methods to enable characteristic pattern mining,
 * i.e., those where the support is a map: category -> support.
 *
 * @param <G> graph type
 */
interface CharacteristicSupport<G extends WithGraph & WithCategories>
    extends SubgraphMiningSupport<G, Map<Integer, Long>>, DmgmOperator {

  @Override
  default Map<Integer, Long> getMinSupportAbsolute(
      Collection<CachedGraph> input, float minSupportRel) {

    PropertyGraphDb db = getDatabase();

    return getParallelizableStream(input)
        .map(g -> db.getString(g.getId(), getCategoryKey()))
        .map(c -> c == null ? DmgmConstants.PropertyDefaultValues.STRING : c)
        .map(db::encode)
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            e -> getAbsoluteSupport(e.getValue(), minSupportRel)));
  }

  @Override
  default <K, E extends WithEmbedding> Stream<Pair<K, Map<Integer, Long>>> addSupportAndFilter(
      Map<K, List<E>> patternEmbeddings, Map<Integer, Long> minSupportAbsolute,
      Map<Long, G> graphIndex, boolean parallel) {

    Set<Map.Entry<K, List<E>>> entrySet = patternEmbeddings.entrySet();

    Stream<Map.Entry<K, List<E>>> stream = parallel
        ? entrySet.parallelStream()
        : entrySet.stream();

    return stream
        .map(entry -> addSupport(entry, graphIndex))
        .filter(p -> p.getValue()
          .entrySet()
          .stream()
          .anyMatch(e -> e.getValue() >= minSupportAbsolute.get(e.getKey()))
        );
  }

  /**
   * Calculate support of a single pattern.
   * TODO: Make private to the interface.
   *
   * @param entry map: pattern -> embedding...
   * @param graphIndex map: graph id -> graph
   * @param <K> pattern type
   * @param <E> embedding type
   * @return (pattern, map: category -> support)
   */
  default <K, E extends WithEmbedding> Pair<K, Map<Integer, Long>> addSupport(
      Map.Entry<K, List<E>> entry, Map<Long, G> graphIndex) {

    Map<Integer, Long> support = entry.getValue()
        .stream()
        .map(WithEmbedding::getEmbedding)
        .mapToLong(DfsEmbedding::getGraphId)
        .distinct()
        .mapToObj(i -> graphIndex.get(i).getCategories())
        .flatMapToInt(IntStream::of)
        .boxed()
        .collect(Collectors.groupingBy(
            Function.identity(),
            Collectors.counting()));

    return new Pair<>(entry.getKey(), support);
  }

  @Override
  default long[] output(List<Pair<DfsCode, Map<Integer, Long>>> frequentPatterns) {
    return frequentPatterns
        .stream()
        .flatMapToLong(ps -> ps.getValue()
          .entrySet()
          .stream()
          .mapToLong(e -> {
            DfsCode dfsCode = ps.getKey();
            int category = e.getKey();
            long support = e.getValue();

            long graphId = createGraph(dfsCode);

            SetProperties setProperties = getDatabase();
            setProperties.set(graphId, getDfsCodeKey(), dfsCode.toString(setProperties));
            setProperties.set(graphId, getCategoryKey(), setProperties.decode(category));
            setProperties.set(graphId, getSupportKey(), support);

            return graphId;
          })
        )
        .toArray();
  }

  /**
   * Get the category of a graph.
   *
   * @param get property getter
   * @param id graph id
   *
   * @return category
   */
  default int[] getCategories(GetProperties get, long id) {
    int[] categories;

    String[] names = get.getStrings(id, getCategoryKey());
    if (names != null) {
      categories = new int[names.length];

      for (int i = 0; i < categories.length; i++) {
        categories[i] = get.encode(names[i]);
      }
    } else {

      String name = get.getString(id, getCategoryKey());

      if (name != null) {
        categories = new int[] {get.encode(name)};
      } else {
        categories = getDefaultCategories();
      }
    }

    return categories;
  }

  /**
   * Get the category property key.
   *
   * @return key
   */
  int getCategoryKey();

  /**
   * Get the default category property value.
   *
   * @return value
   */
  int[] getDefaultCategories();
}
