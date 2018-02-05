package org.biiig.dmgm.impl.operators.subgraph_mining.frequent;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import javafx.util.Pair;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.graph.DFSCode;
import org.biiig.dmgm.impl.operators.subgraph_mining.characteristic.SupportMethodsBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SupportMethods;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.WithGraphId;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class FrequentSupportMethods extends SupportMethodsBase
  implements SupportMethods<Long> {

  private final long minSupportAbsolute;

  public FrequentSupportMethods(GraphDB database, boolean parallel, long minSupportAbsolute) {
    super(database, parallel);
    this.minSupportAbsolute = minSupportAbsolute;
  }

  @Override
  public <K, V extends WithGraphId> Stream<Pair<Pair<K, List<V>>, Long>> aggregateAndFilter(Stream<Pair<K, V>> reports) {

    Set<Map.Entry<K, List<V>>> entrySet = reports
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
  public long[] output(List<Pair<Pair<DFSCode, List<DFSEmbedding>>, Long>> filtered) {
    return filtered
      .stream()
      .mapToLong(p -> {DFSCode dfsCode = p.getKey().getKey();
        long support = p.getValue();
        long graphId = createGraph(database, dfsCode);

        database.set(graphId, dfsCodeKey, dfsCode.toString(database));
        database.set(graphId, supportKey, support);

        return graphId;
      })
      .toArray();
  }

}
