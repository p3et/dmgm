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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Group by a key and list all values.
 * Keys and values can be user defined.
 *
 * @param <I> input type
 * @param <K> key type
 * @param <V> value type
 */
public class GroupByKeyListValues<I, K, V>
    implements Collector<I, Map<K, List<V>>, Map<K, List<V>>> {

  /**
   * f: input -> key.
   */
  private final Function<I, K> keySelector;
  /**
   * f: input -> value.
   */
  private final Function<I, V> valueSelector;

  /**
   * Constructor.
   *
   * @param keySelector input -> key
   * @param valueSelector input -> value
   */
  public GroupByKeyListValues(Function<I, K> keySelector, Function<I, V> valueSelector) {
    this.keySelector = keySelector;
    this.valueSelector = valueSelector;
  }

  @Override
  public Supplier<Map<K, List<V>>> supplier() {
    return Maps::newHashMap;
  }

  @Override
  public BiConsumer<Map<K, List<V>>, I> accumulator() {
    return (map, input) -> {
      K key = keySelector.apply(input);
      V value = valueSelector.apply(input);

      List<V> values = map.get(key);

      if (values == null) {
        map.put(key, Lists.newArrayList(value));
      } else {
        values.add(value);
      }
    };
  }

  @Override
  public BinaryOperator<Map<K, List<V>>> combiner() {
    return (keep, merge) -> {
      for (Map.Entry<K, List<V>> entry : merge.entrySet()) {
        K key = entry.getKey();
        List<V> keepValues = keep.get(key);
        List<V> mergeValues = entry.getValue();

        if (keepValues == null) {
          keep.put(key, mergeValues);
        } else {
          keepValues.addAll(mergeValues);
        }
      }

      return keep;
    };
  }

  @Override
  public Function<Map<K, List<V>>, Map<K, List<V>>> finisher() {
    return Function.identity();
  }

  @Override
  public Set<Characteristics> characteristics() {
    return EnumSet.of(Characteristics.UNORDERED, Characteristics.IDENTITY_FINISH);
  }
}
