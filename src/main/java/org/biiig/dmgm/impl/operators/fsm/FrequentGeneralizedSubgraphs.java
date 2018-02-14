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
import org.biiig.dmgm.impl.operators.fsm.common.DFSCode;
import org.biiig.dmgm.impl.operators.fsm.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.fsm.common.SubgraphMiningBase;
import org.biiig.dmgm.impl.operators.fsm.simple.FrequentSupportMethods;
import org.biiig.dmgm.impl.operators.fsm.generalized.EmbeddingWithTaxonomyPaths;
import org.biiig.dmgm.impl.operators.fsm.generalized.GraphWithTaxonomyPaths;
import org.biiig.dmgm.impl.operators.fsm.generalized.GeneralizedSubgraphs;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FrequentGeneralizedSubgraphs
  extends SubgraphMiningBase<GraphWithTaxonomyPaths, EmbeddingWithTaxonomyPaths, Long>
  implements GeneralizedSubgraphs<GraphWithTaxonomyPaths, EmbeddingWithTaxonomyPaths, Long>, FrequentSupportMethods {

  /**
   * Constructor.
   *
   * @param db            database
   * @param parallel      flag to enable parallel execution
   * @param minSupportRel minimum support threshold
   * @param maxEdgeCount  maximum result edge count
   */
  protected FrequentGeneralizedSubgraphs(PropertyGraphDB db, boolean parallel, float minSupportRel, int maxEdgeCount) {
    super(db, parallel, minSupportRel, maxEdgeCount);
  }

  @Override
  public Stream<GraphWithTaxonomyPaths> preProcess(Collection<CachedGraph> input) {
    Map<Integer, int[]> taxonomyPathIndex = getTaxonomyPathIndex(database, input);


    return getParallelizableStream(input)
      .map(graph -> new GraphWithTaxonomyPaths(graph, getTaxonomyPaths(graph, taxonomyPathIndex)));
  }

  @Override
  public long[] output(
    List<Pair<DFSCode, Long>> frequentPatterns, Map<DFSCode, List<EmbeddingWithTaxonomyPaths>> patternEmbeddings, Long minSupportAbsolute) {

    // specialize
    frequentPatterns = getParallelizableStream(frequentPatterns)
      .flatMap(p -> getFrequentSpecializations(p.getKey(), patternEmbeddings.get(p.getKey()), p.getValue(), minSupportAbsolute))
      .collect(Collectors.toList());

    // use output of characteristic subgraphs
    return output(frequentPatterns);
  }

  @Override
  public BiFunction<GraphWithTaxonomyPaths, DFSEmbedding, EmbeddingWithTaxonomyPaths> getEmbeddingFactory() {
    return (g, e) -> new EmbeddingWithTaxonomyPaths(e, g.getTaxonomyPaths());
  }
}
