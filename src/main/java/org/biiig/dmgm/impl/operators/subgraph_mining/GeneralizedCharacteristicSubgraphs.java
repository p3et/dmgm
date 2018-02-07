package org.biiig.dmgm.impl.operators.subgraph_mining;

import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.SpecializableCachedGraph;
import org.biiig.dmgm.impl.operators.subgraph_mining.characteristic.CategorySupportMethods;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SupportMethods;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.PropertyKeys;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GeneralizedCharacteristicSubgraphs
  extends GeneralizedSubgraphsBase<Map<Integer, Long>> {

  private static final String DEFAULT_CATEGORY = "_default";

  private final int defaultCategory;
  private final int categoryKey;


  public GeneralizedCharacteristicSubgraphs(GraphDB database, float minSupportRel, int maxEdgeCount) {
    super(maxEdgeCount, database, minSupportRel);
    defaultCategory = database.encode(DEFAULT_CATEGORY);
    categoryKey = database.encode(PropertyKeys.CATEGORY);
  }

  @Override
  public SupportMethods getAggregateAndFilter(Map<Long, SpecializableCachedGraph> input) {
    Map<Long, int[]> graphCategories = getGraphCategories(input);
    Map<Integer, Long> categoryMinSupport = getCategoryMinSupport(graphCategories);
    return new CategorySupportMethods(database, parallel, graphCategories, categoryMinSupport, categoryKey);
  }

  private Map<Integer, Long> getCategoryMinSupport(Map<Long, int[]> graphCategories) {
    Map<Integer, Long> categoryCounts = graphCategories
      .values()
      .stream()
      .flatMapToInt(IntStream::of)
      .boxed()
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    return categoryCounts
      .entrySet()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey, e -> (long) Math.round(e.getValue() * minSupportRel)));
  }

  private Map<Long, int[]> getGraphCategories(Map<Long, SpecializableCachedGraph> input) {
    return input
      .values()
      .stream()
      .collect(Collectors.toMap(
        CachedGraph::getId,
        g -> {
          String categoryString = database.getString(g.getId(), categoryKey);
          int category = categoryString == null ? defaultCategory : database.encode(categoryString);
          return new int[]{category};
        }));
  }

}
