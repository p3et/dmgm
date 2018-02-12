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

package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import org.biiig.dmgm.api.db.CachedGraph;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.Preprocessor;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class PreprocessorBase implements Preprocessor {
  protected final float minSupport;

  public PreprocessorBase(float minSupport) {
    this.minSupport = minSupport;
  }

  protected Set<Integer> getFrequentLabels(Stream<CachedGraph> graphs, Function<CachedGraph, Stream<Integer>> labelSelector, Integer minSupportAbsolute) {
    return graphs
          .flatMap(labelSelector)
          .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
          .entrySet()
          .stream()
          .filter(e -> e.getValue() >= minSupportAbsolute)
          .map(Map.Entry::getKey)
          .collect(Collectors.toSet());
  }
}
