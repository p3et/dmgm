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

package org.biiig.dmgm.impl.util.collectors;

import com.google.common.collect.Maps;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Group by a key and sum all long values.
 * Keys and values are user defined.
 *
 * @param <I> input type
 * @param <K> key type
 */
public class GroupByKeySumLong<I, K>
    implements Collector<I, Map<K, Long>, Map<K, Long>> {

  /**
   * f: input -> key.
   */
  private final Function<I, K> keySelector;
  /**
   * f: input -> long.
   */
  private final Function<I, Long> valueSelector;

  /**
   * Constructor.
   *
   * @param keySelector input -> key
   * @param valueSelector input -> value
   */
  public GroupByKeySumLong(Function<I, K> keySelector, Function<I, Long> valueSelector) {
    this.keySelector = keySelector;
    this.valueSelector = valueSelector;
  }

  @Override
  public Supplier<Map<K, Long>> supplier() {
    return Maps::newHashMap;
  }

  @Override
  public BiConsumer<Map<K, Long>, I> accumulator() {
    return (map, input) -> {
      K key = keySelector.apply(input);
      Long increment = valueSelector.apply(input);

      Long sum = map.get(key);
      sum = sum == null ? increment : sum + increment;

      map.put(key, sum);
    };
  }

  @Override
  public BinaryOperator<Map<K, Long>> combiner() {
    return (keep, merge) -> {
      for (Map.Entry<K, Long> entry : merge.entrySet()) {
        K key = entry.getKey();
        Long sum = keep.get(key);
        Long increment = entry.getValue();

        sum = sum == null ? increment : sum + increment;

        keep.put(key, sum);
      }

      return keep;
    };
  }

  @Override
  public Function<Map<K, Long>, Map<K, Long>> finisher() {
    return Function.identity();
  }

  @Override
  public Set<Characteristics> characteristics() {
    return EnumSet.of(Characteristics.UNORDERED, Characteristics.IDENTITY_FINISH);
  }
}
