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

package org.biiig.dmgm.impl.operators.fsm;

import javafx.util.Pair;
import org.biiig.dmgm.api.db.SetProperties;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.impl.operators.DMGMOperator;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public interface FrequentSupportMethods<G extends WithGraph>
  extends SubgraphMiningSupportMethods<G, Long>, DMGMOperator {

  @Override
  default Long getMinSupportAbsolute(Collection<CachedGraph> input, float minSupportRel) {
    return getAbsoluteSupport(input.size(), minSupportRel);
  }

  @Override
  default <K, E extends WithDFSEmbedding> Stream<Pair<K, Long>> addSupportAndFilter(
    Map<K, List<E>> patternEmbeddings, Long minSupportAbsolute, Map<Long, G> graphIndex,  boolean parallel) {

    Set<Map.Entry<K, List<E>>> entrySet = patternEmbeddings
      .entrySet();

    Stream<Map.Entry<K, List<E>>> stream = parallel ?
      entrySet.parallelStream() :
      entrySet.stream();

    return stream
      .map(this::addSupport)
      .filter(p -> p.getValue() >= minSupportAbsolute);
  }

  default <K, F extends WithDFSEmbedding> Pair<K, Long> addSupport(Map.Entry<K, List<F>> entry) {

    Long support = entry.getValue()
      .stream()
      .mapToLong(e -> e.getEmbedding().getGraphId())
      .distinct()
      .count();

    return new Pair<>( entry.getKey(), support);
  }

  @Override
  default long[] output(List<Pair<DFSCode, Long>> frequentPatterns) {
    return frequentPatterns
      .stream()
      .mapToLong( ps -> {
        DFSCode dfsCode = ps.getKey();
        long support = ps.getValue();

        long graphId = createGraph(dfsCode);

        SetProperties setProperties = getDatabase();
        setProperties.set(graphId, getDfsCodeKey(), dfsCode.toString(setProperties));
        setProperties.set(graphId, getSupportKey(), support);

        return graphId;
      })
      .toArray();
  }
}
