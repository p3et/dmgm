package org.biiig.dmgm.impl.operators.subgraph_mining;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.graph.DFSCode;
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

    Map<Long, CachedGraph> indexedGraphs = (parallel ? input.parallelStream() : input.stream())
      .collect(Collectors.toMap(CachedGraph::getId, Function.identity()));

    SupportMethods<S> supportMethods = getAggregateAndFilter(input);

    Map<Long, int[][]> graphDimensionPaths = generalize(input);

    Stream<Pair<DFSCode,DFSEmbedding>> reports = getSingleEdgeReports(input);



    Stream<Pair<Pair<DFSCode,List<DFSEmbedding>>, S>> filtered =
      supportMethods.aggregateAndFilter(reports);

    List<Pair<Pair<DFSCode,List<DFSEmbedding>>, S>> parents = specialize(filtered, supportMethods, graphDimensionPaths);

    long[] graphIds = supportMethods.output(parents);

    int edgeCount = 1;
    while (edgeCount < maxEdgeCount && !parents.isEmpty()) {
      reports = (parallel ? parents.parallelStream() : parents.stream())
        .map(Pair::getKey)
        .flatMap(new GrowAllChildren(indexedGraphs));

      filtered = supportMethods.aggregateAndFilter(reports)
        .filter(e -> new IsMinimal().test(e.getKey().getKey()));

      parents = specialize(filtered, supportMethods, graphDimensionPaths);

      graphIds = ArrayUtils.addAll(graphIds, supportMethods.output(parents));

      edgeCount++;
    }

    return database.createCollection(collectionLabel, graphIds);
  }

  public abstract SupportMethods<S> getAggregateAndFilter(List<CachedGraph> input);

  private Map<Long, int[][]> generalize(List<CachedGraph> input) {
    Map<Integer, Long> vertexLabelSupport = getVertexLabelSupport(input);
    Map<Integer, int[]> dimensionPathMap = getDimensionPathMap(vertexLabelSupport);

    return (parallel ? input.parallelStream() : input.stream())
      .collect(Collectors.toMap(
        CachedGraph::getId,
        g -> {
          int[][] dimensionPaths = new int[g.getVertexCount()][];

          g.vertexIdStream()
            .forEach(v -> {
              int[] dimensionPath = dimensionPathMap.get(g.getVertexLabel(v));
              dimensionPaths[v] = dimensionPath;
              g.setVertexLabel(v, dimensionPath[0]);
            });

          return dimensionPaths;
        }
        ));
  }

  private List<Pair<Pair<DFSCode,List<DFSEmbedding>>, S>> specialize(
    Stream<Pair<Pair<DFSCode, List<DFSEmbedding>>, S>> filtered, SupportMethods supportMethods, Map<Long, int[][]> graphDimensionPaths) {

    return filtered
      .flatMap(new FrequentSpecializations<S>(graphDimensionPaths, supportMethods))
      .collect(Collectors.toList());
  }



  public Stream<Pair<DFSCode,DFSEmbedding>> getSingleEdgeReports(List<CachedGraph> input) {
    return (parallel ? input.parallelStream() : input.stream())
      .flatMap(new InitializeParents(patternLabel));
  }

  public Map<Integer, int[]> getDimensionPathMap(Map<Integer, Long> vertexLabelSupport) {
    return vertexLabelSupport
      .keySet()
      .stream()
      .collect(
        Collectors.toMap(
          Function.identity(),
          l -> {
            int[] dimensionPath = new int[] {l};

            String child = database.decode(l);

            if (child.contains(LEVEL_SEPARATOR)) {
              while (child.contains(LEVEL_SEPARATOR)) {
                String parent = StringUtils.substringBeforeLast(child, LEVEL_SEPARATOR);
                dimensionPath = ArrayUtils.add(dimensionPath, database.encode(parent));
                child = parent;
              }

              ArrayUtils.reverse(dimensionPath);
            }

            return dimensionPath;
          }));

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
