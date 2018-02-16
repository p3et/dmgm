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
import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.api.model.CachedGraph;

/**
 * This algorithm extracts subgraph pattern
 * that are frequent in at least one category of input graphs.
 *
 * @see <a href="https://www.degruyter.com/view/j/itit.2016.58.issue-4/itit-2016-0006/itit-2016-0006.xml">Characteristic Subgraph Mining</a>
 */
public class CharacteristicSimpleSubgraphs
    extends CharacteristicSubgraphsBase<GraphWithCategories> {

  /**
   * Constructor.
   *
   * @param db            database
   * @param parallel      flag to enable parallel execution
   * @param minSupportRel minimum support threshold
   * @param maxEdgeCount  maximum result edge count
   */
  CharacteristicSimpleSubgraphs(
      PropertyGraphDb db, boolean parallel, float minSupportRel, int maxEdgeCount) {

    super(db, parallel, minSupportRel, maxEdgeCount);
  }

  @Override
  public Stream<GraphWithCategories> preProcess(Collection<CachedGraph> input) {
    return getParallelizableStream(input)
      .map(graph -> new GraphWithCategories(graph, getCategories(database, graph.getId())));
  }

  @Override
  public long[] output(List<Pair<DfsCode, Map<Integer, Long>>> frequentPatterns,
                       Map<DfsCode, List<WithEmbedding>> patternEmbeddings,
                       Map<Long, GraphWithCategories> graphIndex,
                       Map<Integer, Long> minSupportAbsolute) {

    // use output of characteristic subgraphs
    return output(frequentPatterns);
  }
}
