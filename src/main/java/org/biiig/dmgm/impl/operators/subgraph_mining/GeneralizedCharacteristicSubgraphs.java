package org.biiig.dmgm.impl.operators.subgraph_mining;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.graph.DFSCode;
import org.biiig.dmgm.impl.operators.CollectionOperatorBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.characteristic.CharacteristicAggregateAndFilter;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.AggregateAndFilter;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.GrowAllChildren;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.InitializeParents;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.IsMinimal;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.PropertyKeys;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.FrequentSpecializations;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GeneralizedCharacteristicSubgraphs extends CollectionOperatorBase {

  private static final String DEFAULT_CATEGORY = "_default";
  private static final String LEVEL_SEPARATOR = "_";
  private static final String FREQUENT_SUBGRAPH = "Frequent Subgraph";
  private static final String FREQUENT_SUBGRAPHS = "Frequent Subgraphs";
  private static final String DFS_CODE = "_dfsCode";
  private final GraphDB database;
  private final float minSupportRel;
  private final int maxEdgeCount;
  private final int categoryKey;
  private final int defaultCategory;
  private final int patternLabel;
  private final int collectionLabel;
  private final int dfsCodeKey;
  private final int supportKey;

  public GeneralizedCharacteristicSubgraphs(GraphDB database, float minSupportRel, int maxEdgeCount) {
    this.database = database;
    this.minSupportRel = minSupportRel;
    this.maxEdgeCount = maxEdgeCount;
    categoryKey = database.encode(PropertyKeys.CATEGORY);
    defaultCategory = database.encode(DEFAULT_CATEGORY);
    patternLabel = database.encode(FREQUENT_SUBGRAPH);
    collectionLabel = database.encode(FREQUENT_SUBGRAPHS);
    dfsCodeKey = database.encode(DFS_CODE);
    supportKey = database.encode(PropertyKeys.SUPPORT);
  }

  @Override
  public Long apply(Long collectionId) {
    List<CachedGraph> input = database.getCachedCollection(collectionId);

    Map<Long, CachedGraph> indexedGraphs = (parallel ? input.parallelStream() : input.stream())
      .collect(Collectors.toMap(CachedGraph::getId, Function.identity()));

    Map<Long, int[]> graphCategories = getGraphCategories(input);
    Map<Integer, Long> categoryMinSupport = getCategoryMinSupport(graphCategories);

    Map<Long, int[][]> graphDimensionPaths = generalize(input);

    Stream<Pair<DFSCode,DFSEmbedding>> reports = getSingleEdgeReports(input);

    AggregateAndFilter aggregateAndFilter = new CharacteristicAggregateAndFilter(graphCategories, categoryMinSupport);

    Stream<Pair<Pair<DFSCode,List<DFSEmbedding>>, Map<Integer, Long>>> filtered = 
      aggregateAndFilter.aggregateAndFilter(reports);

    List<Pair<Pair<DFSCode,List<DFSEmbedding>>, Map<Integer, Long>>> parents = specialize(filtered, aggregateAndFilter, graphDimensionPaths);

    long[] graphIds = output(parents);
    
    int edgeCount = 1;
    while (edgeCount < maxEdgeCount && !parents.isEmpty()) {
      reports = (parallel ? parents.parallelStream() : parents.stream())
        .map(Pair::getKey)
        .flatMap(new GrowAllChildren(indexedGraphs));

      filtered = aggregateAndFilter.aggregateAndFilter(reports)
        .filter(e -> new IsMinimal().test(e.getKey().getKey()));
      
      parents = specialize(filtered, aggregateAndFilter, graphDimensionPaths);

      graphIds = ArrayUtils.addAll(graphIds, output(parents));

      edgeCount++;
    }

    return database.createCollection(collectionLabel, graphIds);
  }

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

  private List<Pair<Pair<DFSCode,List<DFSEmbedding>>, Map<Integer, Long>>> specialize(
    Stream<Pair<Pair<DFSCode, List<DFSEmbedding>>, Map<Integer, Long>>> filtered, AggregateAndFilter aggregateAndFilter, Map<Long, int[][]> graphDimensionPaths) {

    return filtered
      .flatMap(new FrequentSpecializations(graphDimensionPaths, aggregateAndFilter))
      .collect(Collectors.toList());
  }

  private long[] output(List<Pair<Pair<DFSCode,List<DFSEmbedding>>, Map<Integer, Long>>> filtered) {
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

  private Map<Long, int[]> getGraphCategories(List<CachedGraph> input) {
    return input
      .stream()
      .collect(Collectors.toMap(
        CachedGraph::getId,
        g -> {
          String categoryString = database.getString(g.getId(), categoryKey);
          int category = categoryString == null ? defaultCategory : database.encode(categoryString);
          return new int[]{category};
        }));
  }

  public long createGraph(GraphDB db, CachedGraph graph) {
    long[] vertexIds = graph
      .vertexIdStream()
      .map(graph::getVertexLabel)
      .mapToLong(db::createVertex)
      .toArray();

    long[] edgeIds = graph
      .edgeIdStream()
      .mapToLong(edgeId -> {
        int label = graph.getEdgeLabel(edgeId);
        long sourceId = vertexIds[graph.getSourceId(edgeId)];
        long targetId = vertexIds[graph.getTargetId(edgeId)];
        return db.createEdge(label, sourceId, targetId);
      })
      .toArray();

    return db.createGraph(graph.getLabel(), vertexIds, edgeIds);
  }
}
