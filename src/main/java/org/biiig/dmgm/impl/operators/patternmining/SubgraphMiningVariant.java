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

package org.biiig.dmgm.impl.operators.patternmining;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javafx.util.Pair;

import org.biiig.dmgm.api.model.CachedGraph;

/**
 * The methods of a subgraph mining algorithm that are related
 * to the extracted patterns.
 *
 * @param <G> graph type
 * @param <S> support type
 */
public interface SubgraphMiningVariant<G extends WithGraph, S> {

  /**
   * Only for generalized variants: Generalize vertex labels and add taxonomy paths.
   * Only for characteristic variants: Categorize graphs
   *
   * @param input raw cached graphs
   * @return graphs with additional information
   */
  Stream<G> preProcess(Collection<CachedGraph> input);

  /**
   * Write patterns to the database.
   *
   * @param frequentPatterns map: frequent patterns -> support
   * @param patternEmbeddings map: pattern -> embedding
   * @param graphIndex map: graph id -> graph
   * @param minSupportAbsolute min support threshold
   *
   * @return ids of the generated graph ids
   */
  long[] output(
      List<Pair<DfsCode, S>> frequentPatterns, Map<DfsCode, List<WithEmbedding>> patternEmbeddings,
      Map<Long, G> graphIndex, S minSupportAbsolute);
}
