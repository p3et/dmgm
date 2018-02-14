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

package org.biiig.dmgm.impl.operators.fsm.characteristic;

import javafx.util.Pair;
import org.biiig.dmgm.api.config.DMGMConstants;
import org.biiig.dmgm.api.db.GetProperties;
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.db.SetProperties;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.impl.operators.DMGMOperator;
import org.biiig.dmgm.impl.operators.fsm.common.DFSCode;
import org.biiig.dmgm.impl.operators.fsm.common.SubgraphMiningSupportMethods;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface CharacteristicSupportMethods
  extends SubgraphMiningSupportMethods<Map<Integer, Long>>, DMGMOperator {

  @Override
  default Map<Integer, Long> getMinSupportAbsolute(Collection<CachedGraph> input, float minSupportRel) {
    PropertyGraphDB db = getDatabase();

    return getParallelizableStream(input)
      .map(g -> db.getString(g.getId(), getCategoryKey()))
      .map(c -> c == null ? DMGMConstants.PropertyDefaultValues.STRING : c)
      .map(db::encode)
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
      .entrySet()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey, e -> getAbsoluteSupport(e.getValue(), minSupportRel)));
  }

  @Override
  default <K, E extends WithEmbeddingAndCategory> Stream<Pair<K, Map<Integer, Long>>> addSupportAndFilter(
    Map<K, List<E>> patternEmbeddings, Map<Integer, Long> minSupportAbsolute, boolean parallel) {

    Set<Map.Entry<K, List<E>>> entrySet = patternEmbeddings
      .entrySet();

    Stream<Map.Entry<K, List<E>>> stream = parallel ?
      entrySet.parallelStream() :
      entrySet.stream();

    return stream
      .map(this::addSupport)
      .filter(p -> p.getValue()
        .entrySet()
        .stream()
        .anyMatch(e -> e.getValue() >= minSupportAbsolute.get(e.getKey()))
      );
  }

  default <K, F extends WithEmbeddingAndCategory> Pair<K, Map<Integer, Long>> addSupport(Map.Entry<K, List<F>> entry) {

    Map<Integer, Long> support = entry.getValue()
      .stream()
      .map(e -> new Pair<>(e.getGraphId(), e.getCategory()))
      .distinct()
      .map(Pair::getValue)
      .collect(Collectors.groupingBy(
        Function.identity(),
        Collectors.counting())
      );

    return new Pair<>( entry.getKey(), support);
  }

  @Override
  default long[] output(List<Pair<DFSCode, Map<Integer, Long>>> frequentPatterns) {
    return frequentPatterns
      .stream()
      .flatMapToLong( ps -> ps.getValue()
        .entrySet()
        .stream()
        .mapToLong(e -> {
          DFSCode dfsCode = ps.getKey();
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
  default int getCategory(GetProperties get, long id) {
    String category = get.getString(id, getCategoryKey());
    return category == null ? getDefaultCategory() : get.encode(category);
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
  int getDefaultCategory();
}
