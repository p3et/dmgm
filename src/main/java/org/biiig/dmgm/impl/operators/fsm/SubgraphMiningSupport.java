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
import java.util.stream.Stream;
import javafx.util.Pair;
import org.biiig.dmgm.api.model.CachedGraph;

/**
 * The methods of a subgraph mining algorithm that are related
 * to support counting and support based filtering.
 *
 * @param <S> support type
 */
public interface SubgraphMiningSupport<G extends WithGraph, S> {

  /**
   * Calculate the absolute min support threshold regarding to the mining variant,
   * i.e., a single value or a map: category -> value
   *
   * @param input cached graph collection
   * @param minSupportRel relative min support threshold
   *
   * @return absolute min support threshold
   */
  S getMinSupportAbsolute(Collection<CachedGraph> input, float minSupportRel);

  /**
   * Generalization of absolute value calculation.
   *
   * @param graphCount graph count
   * @param relativeSupport relative support
   *
   * @return absolute support
   */
  default long getAbsoluteSupport(long graphCount, float relativeSupport) {
    return Math.round((double) graphCount * relativeSupport);
  }

  /**
   * Calculate support and filter patterns based theron.
   *
   * @param <K> pattern type (DFS code or vector)
   * @param <E> embedding type (embedding only or vector with embedding)
   *
   * @param patternEmbeddings map: pattern -> embedding... OR map: -> vector -> vector...
   * @param minSupportAbsolute min support threshold
   * @param graphIndex
   * @param parallel flag to enable parallel execution (should only be true for 1-edge patterns)
   * @return map: frequent pattern -> support
   */
  <K, E extends WithEmbedding> Stream<Pair<K, S>> addSupportAndFilter(
      Map<K, List<E>> patternEmbeddings, S minSupportAbsolute,
      Map<Long, G> graphIndex, boolean parallel);

  /**
   * Write patterns and support information to the database.
   * For characteristic variants one pattern and its support is stored for each category.
   *
   * @param frequentPatterns map: pattern -> support
   *
   * @return ids of the stored graphs
   */
  long[] output(List<Pair<DfsCode, S>> frequentPatterns);

  /**
   * Get property key to store the support value.
   *
   * @return property key
   */
  int getSupportKey();

  /**
   * Get property key to store the pattern's canonical label (DFS code).
   *
   * @return property key
   */
  int getDfsCodeKey();
}
