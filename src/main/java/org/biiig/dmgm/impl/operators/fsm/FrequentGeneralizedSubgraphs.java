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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.util.Pair;
import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.api.model.CachedGraph;

/**
 * This algorithm extracts generalized frequent subgraphs.
 *
 * @see <a href="http://ieeexplore.ieee.org/document/8244685/">Generalized Subgraph Mining</a>
 */
class FrequentGeneralizedSubgraphs
    extends SubgraphMiningBase<GraphWithTaxonomyPaths, Long>
    implements GeneralizedSubgraphs<GraphWithTaxonomyPaths, Long>,
    FrequentSupport<GraphWithTaxonomyPaths> {

  /**
   * Constructor.
   *
   * @param db            database
   * @param parallel      flag to enable parallel execution
   * @param minSupportRel minimum support threshold
   * @param maxEdgeCount  maximum result edge count
   */
  FrequentGeneralizedSubgraphs(
      PropertyGraphDb db, boolean parallel, float minSupportRel, int maxEdgeCount) {

    super(db, parallel, minSupportRel, maxEdgeCount);
  }

  @Override
  public Stream<GraphWithTaxonomyPaths> preProcess(Collection<CachedGraph> input) {
    Map<Integer, int[]> taxonomyPathIndex = getTaxonomyPathIndex(database, input);

    return getParallelizableStream(input)
      .map(graph -> {
        int[][] taxonomyPaths = getTaxonomyPaths(graph, taxonomyPathIndex);

        for (int i = 0; i < graph.getVertexCount(); i++) {
          graph.getVertexLabels()[i] = taxonomyPaths[i][0];
        }

        return new GraphWithTaxonomyPaths(graph, taxonomyPaths);
      });
  }

  @Override
  public long[] output(List<Pair<DfsCode, Long>> frequentPatterns,
                       Map<DfsCode, List<WithEmbedding>> patternEmbeddings,
                       Map<Long, GraphWithTaxonomyPaths> graphIndex, Long minSupportAbs) {

    // specialize
    frequentPatterns = getParallelizableStream(frequentPatterns)
      .flatMap(p -> getFrequentSpecializations(
        p.getKey(), patternEmbeddings.get(p.getKey()), p.getValue(), graphIndex, minSupportAbs))
      .collect(Collectors.toList());

    // use output of characteristic subgraphs
    return output(frequentPatterns);
  }
}
