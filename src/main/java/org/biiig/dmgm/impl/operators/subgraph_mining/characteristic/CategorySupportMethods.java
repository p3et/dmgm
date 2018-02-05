package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import javafx.util.Pair;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.graph.DFSCode;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SupportMethods;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.PropertyKeys;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.WithGraphId;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CategorySupportMethods extends SupportMethodsBase
  implements SupportMethods<Map<Integer, Long>> {

  private final Map<Long, int[]> graphCategories;
  private final Map<Integer, Long> categoryMinSupport;
  protected final int categoryKey;


  public CategorySupportMethods(GraphDB database, Map<Long, int[]> graphCategories, Map<Integer, Long> categoryMinSupport, int categoryKey) {
    super(database);
    this.graphCategories = graphCategories;
    this.categoryMinSupport = categoryMinSupport;
    this.categoryKey = categoryKey;
  }

  @Override
  public <K, V extends WithGraphId> Stream<Pair<Pair<K, List<V>>, Map<Integer, Long>>> aggregateAndFilter(Stream<Pair<K, V>> reports) {

    return reports
      .collect(new GroupByKeyListValues<>(Pair::getKey, Pair::getValue))
      .entrySet()
      .stream()
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
