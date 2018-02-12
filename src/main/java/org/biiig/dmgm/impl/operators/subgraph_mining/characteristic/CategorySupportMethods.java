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

package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import javafx.util.Pair;
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.impl.model.DFSCode;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SupportMethods;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.WithGraphId;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CategorySupportMethods extends SupportMethodsBase
  implements SupportMethods<Map<Integer, Long>> {

  private final Map<Long, int[]> graphCategories;
  private final Map<Integer, Long> categoryMinSupport;
  protected final int categoryKey;


  public CategorySupportMethods(PropertyGraphDB database, boolean parallel, Map<Long, int[]> graphCategories, Map<Integer, Long> categoryMinSupport, int categoryKey) {
    super(database, parallel);
    this.graphCategories = graphCategories;
    this.categoryMinSupport = categoryMinSupport;
    this.categoryKey = categoryKey;
  }

  @Override
  public <K, V extends WithGraphId> Stream<Pair<Pair<K, List<V>>, Map<Integer, Long>>> aggregateAndFilter(Stream<Pair<K, V>> reports) {

    Set<Map.Entry<K, List<V>>> entrySet = reports
      .collect(new GroupByKeyListValues<>(Pair::getKey, Pair::getValue))
      .entrySet();

    Stream<Map.Entry<K, List<V>>> stream = parallel ?
      entrySet.parallelStream() :
      entrySet.stream();

    return stream
      .map(e -> new Pair<>(
        new Pair<>(e.getKey(), e.getValue()),
        e.getValue().stream()
          .map(WithGraphId::getGraphId)
          .distinct()
          .map(graphCategories::get)
          .flatMapToInt(IntStream::of)
          .boxed()
          .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
      ))
      .filter(p -> p.getValue()
        .entrySet()
        .stream()
        .anyMatch(e -> e.getValue() >= categoryMinSupport.get(e.getKey())));
  }

  @Override
  public long[] output(List<Pair<Pair<DFSCode, List<DFSEmbedding>>, Map<Integer, Long>>> filtered) {
    return filtered
      .stream()
      .flatMapToLong(sp -> sp
        .getValue()
        .entrySet()
        .stream()
        .mapToLong(cs -> {
          DFSCode dfsCode = sp.getKey().getKey();
          int category = cs.getKey();
          long support = cs.getValue();

          long graphId = createGraph(database, dfsCode);

          database.set(graphId, dfsCodeKey, dfsCode.toString(database));
          database.set(graphId, categoryKey, database.decode(category));
          database.set(graphId, supportKey, support);

          return graphId;
        }))
      .toArray();
  }

}
