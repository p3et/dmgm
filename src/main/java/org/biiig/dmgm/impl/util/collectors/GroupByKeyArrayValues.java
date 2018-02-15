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
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Group by a key and create an array of all values.
 * Keys and values can be user defined.
 *
 * @param <I> input type
 * @param <K> key type
 * @param <V> value type
 */
public class GroupByKeyArrayValues<I, K, V> implements Collector<I, Map<K, V[]>, Map<K, V[]>> {

  /**
   * f: input -> key.
   */
  private final Function<I, K> keySelector;

  /**
   * f: input -> value.
   */
  private final Function<I, V> valueSelector;

  /**
   * Value class.
   */
  private final Class<V> valueClass;


  /**
  * Constructor.
  *
  * @param keySelector input -> key
  * @param valueSelector input -> value
  * @param valueClass value class
  */
  public GroupByKeyArrayValues(
      Function<I, K> keySelector, Function<I, V> valueSelector, Class<V> valueClass) {

    this.keySelector = keySelector;
    this.valueSelector = valueSelector;
    this.valueClass = valueClass;
  }

  @Override
  public Supplier<Map<K, V[]>> supplier() {
    return Maps::newHashMap;
  }

  @Override
  @SuppressWarnings("unchecked")
  public BiConsumer<Map<K, V[]>, I> accumulator() {
    return (map, input) -> {
      K key = keySelector.apply(input);
      V value = valueSelector.apply(input);
      V[] values = map.get(key);

      if (values == null) {
        values = (V[]) Array.newInstance(valueClass, 1);
        values[0] = value;
      } else {
        values = ArrayUtils.add(values, value);
      }

      map.put(key, values);
    };
  }

  @Override
  public BinaryOperator<Map<K, V[]>> combiner() {
    return (keep, merge) -> {
      for (Map.Entry<K, V[]> entry : merge.entrySet()) {
        K key = entry.getKey();
        V[] keepValues = keep.get(key);
        V[] mergeValues = entry.getValue();

        if (keepValues != null) {
          mergeValues = ArrayUtils.addAll(keepValues, mergeValues);
        }

        keep.put(key, mergeValues);
      }

      return keep;
    };
  }

  @Override
  public Function<Map<K, V[]>, Map<K, V[]>> finisher() {
    return Function.identity();
  }

  @Override
  public Set<Characteristics> characteristics() {
    return EnumSet.of(Characteristics.UNORDERED, Characteristics.IDENTITY_FINISH);
  }
}
