//package org.biiig.dmgm.impl.operators.subgraph_mining.common;
//
//import com.google.common.collect.Maps;
//import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
//import org.biiig.dmgm.api.model.CachedGraph;
//import org.biiig.dmgm.api.GraphDB;
//import org.biiig.dmgm.impl.model.DFSCode;
//import org.biiig.dmgm.impl.operators.CollectionOperatorBase;
//
//import java.util.List;
//import java.util.Map;
//import java.util.function.BiConsumer;
//import java.util.function.Function;
//import java.util.function.Predicate;
//import java.util.stream.Stream;
//
//public abstract class SubgraphMiningBase extends CollectionOperatorBase {
//  private static final GroupByKeyListValues<DFSCodeEmbeddingPair, DFSCode, DFSEmbedding> AGGREGATION =
//    new GroupByKeyListValues<>(DFSCodeEmbeddingPair::getDfsCode, DFSCodeEmbeddingPair::getEmbedding);
//
//  private static final Predicate<DFSCode> IS_MINIMAL = new IsMinimal();
//  protected static final Predicate<Map.Entry<DFSCode, List<DFSEmbedding>>> VERIFICATION = e -> IS_MINIMAL.test(e.getKey());
//
//  private static final String FSG_LABEL = "Frequent Subgraph";
//  private static final String FSGS_LABEL = "Frequent Subgraphs";
//
//  protected final float minSupport;
//  private final int maxEdgeCount;
//  protected final GraphDB database;
//  protected final int supportKey;
//  protected final int frequencyKey;
//  private final int dfsCodeKey;
//  private final int patternLabel;
//  private final int outputCollectionLabel;
//  private final Function<CachedGraph, Stream<DFSCodeEmbeddingPair>> initializeParents;
//
//  protected Map<DFSCode, BiConsumer<GraphDB, Long>> output = Maps.newConcurrentMap();
//
//
//  public SubgraphMiningBase(GraphDB database, float minSupport, int maxEdgeCount) {
//    this.database = database;
//    this.minSupport = minSupport;
//    this.maxEdgeCount = maxEdgeCount;
//
//    // DECODE PROPERTY KEYS
//
//    supportKey = database.encode(PropertyKeys.SUPPORT);
//    frequencyKey = database.encode(PropertyKeys.FREQUENCY);
//    dfsCodeKey = database.encode(PropertyKeys.DFS_CODE);
//    patternLabel = database.encode(FSG_LABEL);
//    outputCollectionLabel = database.encode(FSGS_LABEL);
//
//    initializeParents = new InitializeParents(patternLabel);
//
//  }
//
//  // ORCHESTRATION
//  @Override
//  public Long apply(Long collectionId) {
//    // PREPARE INPUT AND OUTPUT
//
//    List<CachedGraph> collection = database.getCachedCollection(collectionId);
//
//    Map<Long, CachedGraph> input;
//
//    input = preProcess(collection);
//
//    // INIT MINING FUNCTIONS
//
//
//    GrowAllChildren growAllChildren = new GrowAllChildren(input);
//
//    // MINE 1-EDGE SUBGRAPHS
//
//    Map<DFSCode, List<DFSEmbedding>> candidates = input
//      .values()
//      .stream()
//      .flatMap(initializeParents)
//      .collect(AGGREGATION);
//
//    List<DFSCodeEmbeddingsPair> parents = filterAndOutput(candidates, false);
//
//    // MINE n-EDGE SUBGRAPHS
//
//    int edgeCount = 2;
//    while (!parents.isEmpty() && edgeCount <= maxEdgeCount) {
//      candidates = parents
//        .stream()
//        .flatMap(growAllChildren)
//        .collect(AGGREGATION);
//
//      parents = filterAndOutput(candidates, true);
//
//      edgeCount++;
//    }
//
//    // WRITE OUTPUT
//
//    long[] outputGraphIds = output
//      .entrySet()
//      .stream()
//      .mapToLong(entry -> {
//        DFSCode dfsCode = entry.getKey();
//        long id = createGraph(database, dfsCode);
//        database.set(id, dfsCodeKey, dfsCode.toString(database));
//        entry.getValue().accept(database, id);
//        return id;
//      })
//      .toArray();
//
//    return database.createCollection(outputCollectionLabel, outputGraphIds);
//  }
//
//  protected abstract Map<Long, CachedGraph> preProcess(List<CachedGraph> input);
//
//  protected abstract List<DFSCodeEmbeddingsPair> filterAndOutput(
//    Map<DFSCode, List<DFSEmbedding>> candidates, boolean verify);
//}
