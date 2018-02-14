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

package org.biiig.dmgm.impl.operators.subgraph_mining.frequent;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import javafx.util.Pair;
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.impl.operators.fsm.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.characteristic.SupportSpecializationBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCode;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.WithGraphId;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class FrequentSupportSpecialization<G extends CachedGraph> extends SupportSpecializationBase<G, Long>{


  public FrequentSupportSpecialization(PropertyGraphDB db, Long minSupportAbs, boolean parallel) {
    super(db, minSupportAbs, parallel);
  }

  @Override
  public <K, V extends WithGraphId> Stream<Pair<Pair<K, List<V>>, Long>> aggregateAndFilter(Stream<Pair<K, V>> candidates) {

    Set<Map.Entry<K, List<V>>> entrySet = candidates
      .collect(new GroupByKeyListValues<>(Pair::getKey, Pair::getValue))
      .entrySet();

    Stream<Map.Entry<K, List<V>>> stream = parallel ?
      entrySet.parallelStream() :
      entrySet.stream();

    return stream
      .map(e ->
        new Pair<>(
          new Pair<>(e.getKey(), e.getValue()),
          e.getValue()
            .stream()
            .map(WithGraphId::getGraphId)
            .distinct()
            .count()))
      .filter(p ->  p.getValue() >= minSupportAbsolute);
  }

  @Override
  public long[] output(Pair<Pair<DFSCode, List<DFSEmbedding>>, Long> pair) {
    DFSCode dfsCode = pair.getKey().getKey();
    long support = pair.getValue();
    long graphId = createGraph(db, dfsCode);

    db.set(graphId, dfsCodeKey, dfsCode.toString(db));
    db.set(graphId, supportKey, support);

    return new long[] {graphId};
  }
}
