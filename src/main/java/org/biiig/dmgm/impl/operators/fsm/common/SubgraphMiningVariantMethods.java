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

package org.biiig.dmgm.impl.operators.fsm.common;

import javafx.util.Pair;
import org.biiig.dmgm.api.model.CachedGraph;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public interface SubgraphMiningVariantMethods<G extends WithGraph, E extends WithEmbedding, S> {
  /**
   * Generalize vertex labels and add taxonomy paths.
   * Only for generalized variant.
   *
   * Categorize graphs.
   * Only for categorized variant
   *
   * @param input raw cached graphs
   * @return graphs with additional information
   */

  Stream<G> preProcess(Collection<CachedGraph> input);

  long[] output(List<Pair<DFSCode, S>> frequentPatterns, Map<DFSCode, List<E>> patternEmbeddings, S minSupportAbsolute);

  BiFunction<G, DFSEmbedding, E> getEmbeddingFactory();
}
