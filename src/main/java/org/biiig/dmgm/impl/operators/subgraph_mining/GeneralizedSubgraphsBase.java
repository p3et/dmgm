package org.biiig.dmgm.impl.operators.subgraph_mining;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.SpecializableCachedGraph;
import org.biiig.dmgm.impl.graph.DFSCode;
import org.biiig.dmgm.impl.graph.SpecializableAdjacencyList;
import org.biiig.dmgm.impl.operators.CollectionOperatorBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SupportMethods;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.GrowAllChildren;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.InitializeParents;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.IsMinimal;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.FrequentSpecializations;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class GeneralizedSubgraphsBase<S> extends CollectionOperatorBase {
  private static final String LEVEL_SEPARATOR = "_";
  private static final String FREQUENT_SUBGRAPH = "Frequent Subgraph";
  private static final String FREQUENT_SUBGRAPHS = "Frequent Subgraphs";
  protected final GraphDB database;
  protected final int maxEdgeCount;
  protected final int patternLabel;
  protected final int collectionLabel;

  protected final float minSupportRel;


  public GeneralizedSubgraphsBase(int maxEdgeCount, GraphDB database, float minSupportRel) {
    this.minSupportRel = minSupportRel;
    this.maxEdgeCount = maxEdgeCount;
    this.database = database;
    patternLabel = database.encode(FREQUENT_SUBGRAPH);
    collectionLabel = database.encode(FREQUENT_SUBGRAPHS);
  }

  @Override
  public Long apply(Long collectionId) {
    List<CachedGraph> input = database.getCachedCollection(collectionId);

    Map<Integer, Pair<Integer, int[]>> taxonomyPaths = generalize(input);

    Map<Long, SpecializableCachedGraph> indexedGraphs = (parallel ? input.parallelStream() : input.stream())
      .collect(Collectors.toMap(
        CachedGraph::getId,
        g -> {
          int vertexCount = g.getVertexCount();
          int[] vertexLabels = new int[vertexCount];
          int[][] taxonomyTails = new int[vertexCount][];

          for (int i = 0; i < vertexCount; i++) {
            int bottomLevel = g.getVertexLabel(i);
            Pair<Integer, int[]> taxonomyPath = taxonomyPaths.get(bottomLevel);

            if (taxonomyPath == null) {
              vertexLabels[i] = bottomLevel;
            } else {
              vertexLabels[i] = taxonomyPath.getKey();
              taxonomyTails[i] = taxonomyPath.getValue();
            }
          }

          return new SpecializableAdjacencyList(
            g.getId(),
            g.getLabel(),
            vertexLabels,
            g.getEdgeLabels(),
            g.getSourceIds(),
            g.getTargetIds(),
            taxonomyTails);
        }
      ));

    SupportMethods<S> supportMethods = getAggregateAndFilter(input);

    Stream<Pair<DFSCode,DFSEmbedding>> reports = getSingleEdgeReports(input);

    Stream<Pair<Pair<DFSCode,List<DFSEmbedding>>, S>> filtered =
      supportMethods.aggregateAndFilter(reports);

    List<Pair<Pair<DFSCode,List<DFSEmbedding>>, S>> parents = specialize(filtered, supportMethods, indexedGraphs);

    long[] graphIds = supportMethods.output(parents);

    int edgeCount = 1;
    while (edgeCount < maxEdgeCount && !parents.isEmpty()) {
      reports = (parallel ? parents.parallelStream() : parents.stream())
        .map(Pair::getKey)
        .flatMap(new GrowAllChildren(indexedGraphs));

      filtered = supportMethods.aggregateAndFilter(reports)
        .filter(e -> new IsMinimal().test(e.getKey().getKey()));

      parents = specialize(filtered, supportMethods, indexedGraphs);

      graphIds = ArrayUtils.addAll(graphIds, supportMethods.output(parents));

      edgeCount++;
    }

    return database.createCollection(collectionLabel, graphIds);
  }

  public abstract SupportMethods<S> getAggregateAndFilter(List<CachedGraph> input);

  private Map<Integer, Pair<Integer, int[]>> generalize(List<CachedGraph> input) {
    Map<Integer, Long> vertexLabelSupport = getVertexLabelSupport(input);

    return vertexLabelSupport
      .keySet()
      .stream()
      .map(database::decode)
      .filter(s -> s.contains(LEVEL_SEPARATOR))
      .map(s -> {
        int[] ints = new int[0];

        while (s.contains(LEVEL_SEPARATOR)) {
          ints = ArrayUtils.add(ints, database.encode(s));
          s = StringUtils.substringBeforeLast(s, LEVEL_SEPARATOR);
        }

        ArrayUtils.reverse(ints);

        return ints;
      })
      .collect(Collectors.toMap(
        a -> a[a.length - 1],
        a -> new Pair<>(a[0], ArrayUtils.subarray(a, 1, a.length))));
  }

  private List<Pair<Pair<DFSCode,List<DFSEmbedding>>, S>> specialize(
    Stream<Pair<Pair<DFSCode, List<DFSEmbedding>>, S>> filtered, SupportMethods<S> supportMethods, Map<Long, SpecializableCachedGraph> indexedGraphs) {

    return filtered
      .flatMap(new FrequentSpecializations<>(supportMethods, indexedGraphs))
      .collect(Collectors.toList());
  }



  public Stream<Pair<DFSCode,DFSEmbedding>> getSingleEdgeReports(List<CachedGraph> input) {
    return (parallel ? input.parallelStream() : input.stream())
      .flatMap(new InitializeParents(patternLabel));
  }

  public Map<Integer, Long> getVertexLabelSupport(List<CachedGraph> input) {
    return (parallel ? input.parallelStream() : input.stream())
      .flatMapToInt(g -> IntStream
        .of(g.getVertexLabels())
        .distinct())
      .boxed()
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
      .entrySet()
      .stream()
      .map(e -> new Pair<>(database.decode(e.getKey()), e.getValue()))
      .flatMap(p -> {
        String child = p.getKey();

        int[] labels = new int[] {database.encode(child)};

        while (child.contains(LEVEL_SEPARATOR)) {
          String parent = StringUtils.substringBeforeLast(child, LEVEL_SEPARATOR);
          labels = ArrayUtils.add(labels, database.encode(parent));
          child = parent;
        }

        return IntStream.of(labels)
          .mapToObj(parent -> new Pair<>(parent, p.getValue()));
      })
      .collect(new GroupByKeyListValues<>(Pair::getKey, Pair::getValue))
      .entrySet()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().mapToLong(i -> i).sum()));
  }
}
