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

import javafx.util.Pair;
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.impl.operators.fsm.characteristic.CharacteristicSubgraphsBase;
import org.biiig.dmgm.impl.operators.fsm.common.DFSCode;
import org.biiig.dmgm.impl.operators.fsm.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.fsm.mixed.EmbeddingWithCategoryAndTaxonomyPaths;
import org.biiig.dmgm.impl.operators.fsm.mixed.GraphWithCategoryAndTaxonomyPaths;
import org.biiig.dmgm.impl.operators.fsm.generalized.GeneralizedSubgraphs;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CharacteristicGeneralizedSubgraphs
  extends CharacteristicSubgraphsBase<GraphWithCategoryAndTaxonomyPaths, EmbeddingWithCategoryAndTaxonomyPaths>
  implements GeneralizedSubgraphs
  <GraphWithCategoryAndTaxonomyPaths, EmbeddingWithCategoryAndTaxonomyPaths, Map<Integer, Long>> {

  /**
   * Constructor.
   *
   * @param db            database
   * @param parallel      flag to enable parallel execution
   * @param minSupportRel minimum support threshold
   * @param maxEdgeCount  maximum result edge count
   */
  protected CharacteristicGeneralizedSubgraphs(PropertyGraphDB db, boolean parallel, float minSupportRel, int maxEdgeCount) {
    super(db, parallel, minSupportRel, maxEdgeCount);
  }

  @Override
  public Stream<GraphWithCategoryAndTaxonomyPaths> preProcess(Collection<CachedGraph> input) {
    Map<Integer, int[]> taxonomyPathIndex = getTaxonomyPathIndex(database, input);


    return getParallelizableStream(input)
      .map(graph -> {

        int category = getCategory(database, graph.getId());
        int[][] taxonomyPaths = getTaxonomyPaths(graph, taxonomyPathIndex);
        return new GraphWithCategoryAndTaxonomyPaths(graph, taxonomyPaths, category);
      });
  }

  @Override
  public long[] output(
    List<Pair<DFSCode, Map<Integer, Long>>> frequentPatterns, Map<DFSCode, List<EmbeddingWithCategoryAndTaxonomyPaths>> patternEmbeddings, Map<Integer, Long> minSupportAbsolute) {

    // specialize
    frequentPatterns = getParallelizableStream(frequentPatterns)
      .flatMap(p -> getFrequentSpecializations(p.getKey(), patternEmbeddings.get(p.getKey()), p.getValue(), minSupportAbsolute))
      .collect(Collectors.toList());

    // use output of characteristic subgraphs
    return output(frequentPatterns);
  }

  @Override
  public BiFunction<GraphWithCategoryAndTaxonomyPaths, DFSEmbedding, EmbeddingWithCategoryAndTaxonomyPaths> getEmbeddingFactory() {
    return (g, e) -> new EmbeddingWithCategoryAndTaxonomyPaths(e, g.getCategory(), g.getTaxonomyPaths());
  }

}