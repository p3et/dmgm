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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.util.Pair;
import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.api.model.CachedGraph;

/**
 * This algorithm extracts generalized subgraph pattern that
 * are frequent in at least one category of input graphs.
 *
 * @see <a href="http://ieeexplore.ieee.org/document/8244685/">Generalized Subgraph Mining</a>
 * @see <a href="https://www.degruyter.com/view/j/itit.2016.58.issue-4/itit-2016-0006/itit-2016-0006.xml">Characteristic Subgraph Mining</a>
 */
public class CharacteristicGeneralizedSubgraphs
    extends CharacteristicSubgraphsBase<GraphWithCategoriesAndTaxonomyPaths>
    implements GeneralizedSubgraphs<GraphWithCategoriesAndTaxonomyPaths, Map<Integer, Long>> {

  /**
   * Constructor.
   *
   * @param db            database
   * @param parallel      flag to enable parallel execution
   * @param minSupportRel minimum support threshold
   * @param maxEdgeCount  maximum result edge count
   */
  CharacteristicGeneralizedSubgraphs(
      PropertyGraphDb db, boolean parallel, float minSupportRel, int maxEdgeCount) {

    super(db, parallel, minSupportRel, maxEdgeCount);
  }

  @Override
  public Stream<GraphWithCategoriesAndTaxonomyPaths> preProcess(Collection<CachedGraph> input) {
    Map<Integer, int[]> taxonomyPathIndex = getTaxonomyPathIndex(database, input);

    return getParallelizableStream(input)
        .map(graph -> {

          int[] category = getCategories(database, graph.getId());
          int[][] taxonomyPaths = getTaxonomyPaths(graph, taxonomyPathIndex);

          for (int i = 0; i < graph.getVertexCount(); i++) {
            graph.getVertexLabels()[i] = taxonomyPaths[i][0];
          }

          return new GraphWithCategoriesAndTaxonomyPaths(graph, taxonomyPaths, category);
        });
  }

  @Override
  public long[] output(List<Pair<DfsCode, Map<Integer, Long>>> frequentPatterns,
                       Map<DfsCode, List<WithEmbedding>> patternEmbeddings,
                       Map<Long, GraphWithCategoriesAndTaxonomyPaths> graphIndex,
                       Map<Integer, Long> minSupportAbsolute) {

    // specialize
    frequentPatterns = getParallelizableStream(frequentPatterns)
        .flatMap(p -> getFrequentSpecializations(p.getKey(),
            patternEmbeddings.get(p.getKey()), p.getValue(), graphIndex, minSupportAbsolute))
        .collect(Collectors.toList());

    // use output of characteristic subgraphs
    return output(frequentPatterns);
  }

}
