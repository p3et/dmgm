package org.biiig.dmgm.impl.operators.subgraph_mining;

import javafx.util.Pair;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.db.IntPair;
import org.biiig.dmgm.impl.graph.DFSCode;
import org.biiig.dmgm.impl.operators.subgraph.FilterVerticesAndEdgesByLabel;
import org.biiig.dmgm.impl.operators.subgraph_mining.characteristic.AddCategorySupport;
import org.biiig.dmgm.impl.operators.subgraph_mining.characteristic.Characteristic;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.PropertyKeys;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.frequent.AddTotalSupport;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * min frequency is with regard to f
 */
public class CharacteristicSubgraphs extends SubgraphMiningBase implements Characteristic {

  private static final String DEFAULT_CATEGORY = "_default";
  private Map<Long, int[]> graphCategories;
  private Map<Integer, Integer> categoryMinSupport;
  private final int categoryKey;
  private int defaultCategory;

  public CharacteristicSubgraphs(GraphDB database, float minSupport, int maxEdgeCount) {
    super(database, minSupport, maxEdgeCount);
    categoryKey = database.encode(PropertyKeys.CATEGORY);
    defaultCategory = database.encode(DEFAULT_CATEGORY);
  }

  @Override
  protected Map<Long, CachedGraph> preProcess(List<CachedGraph> input) {
    graphCategories = input
      .stream()
      .collect(Collectors.toMap(
        CachedGraph::getId,
        g -> {
          String categoryString = database.getString(g.getId(), categoryKey);
          int category = categoryString == null ? defaultCategory : database.encode(categoryString);
          return new int[]{category};
        }));

    Map<Integer, Long> categoryCounts = graphCategories
      .values()
      .stream()
      .flatMapToInt(IntStream::of)
      .boxed()
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    categoryMinSupport = categoryCounts
      .entrySet()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey, e -> Math.round(e.getValue() * minSupport)));


    Set<Integer> characteristicVertexLabels =
      getCharacteristicLabels(input, CachedGraph::getVertexLabels);

    input = input
      .stream()
      .map(new FilterVerticesAndEdgesByLabel(characteristicVertexLabels::contains, e-> true, true))
      .collect(Collectors.toList());

    Set<Integer> characteristicEdgeLabels =
      getCharacteristicLabels(input, CachedGraph::getEdgeLabels) ;

    return input
      .stream()
      .map(new FilterVerticesAndEdgesByLabel(v -> true, characteristicEdgeLabels::contains, true))
      .collect(Collectors.toMap(CachedGraph::getId, Function.identity()));
  }

  private Set<Integer> getCharacteristicLabels(
    List<CachedGraph> graphs, Function<CachedGraph, int[]> labelSelector) {

    return graphs
      .stream()
      .flatMap(g -> IntStream
        .of(labelSelector.apply(g))
        .distinct()
        .boxed()
        .flatMap(l -> IntStream
          .of(graphCategories.get(g.getId()))
          .mapToObj(c -> new IntPair(c, l))))
      // => (category, label)
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
      .entrySet()
      .stream()
      .filter(e -> e.getValue() >= categoryMinSupport.get(e.getKey().getLeft()))
      // => frequent (category, label)
      .map(Map.Entry::getKey)
      .map(IntPair::getRight)
      // => label
      .collect(Collectors.toSet());
  }


  @Override
  protected List<DFSCodeEmbeddingsPair> filterAndOutput(
    Map<DFSCode, List<DFSEmbedding>> candidates,
    boolean verify) {
    Stream<Map.Entry<DFSCode, List<DFSEmbedding>>> candidateStream = candidates
      .entrySet()
      .stream();

    if (verify)
      candidateStream = candidateStream
        .filter(VERIFICATION);

    Set<DFSCode> characteristicPatterns = candidateStream
      .map(new AddCategorySupport(graphCategories))
      .filter(p -> p.getValue()
        .entrySet()
        .stream()
        .map(e -> e.getValue() >= categoryMinSupport.get(e.getKey()))
        .reduce((a,b) -> a || b).get())
      .peek(p -> p.getValue()
        .forEach((key, value) ->
          output.put(p.getKey(), (db, gid) -> {
            db.set(gid, categoryKey, key);
            db.set(gid, supportKey, Math.toIntExact(value)); })))
      .map(Pair::getKey)
      .collect(Collectors.toSet());

    return candidates
      .entrySet()
      .stream()
      .filter(e -> characteristicPatterns.contains(e.getKey()))
      .map(e -> new DFSCodeEmbeddingsPair(e.getKey(), e.getValue(), 0))
      .collect(Collectors.toList());
  }
}
