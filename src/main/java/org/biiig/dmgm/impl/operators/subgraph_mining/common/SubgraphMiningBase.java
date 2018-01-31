package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import com.google.common.collect.Maps;
import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.impl.graph.DFSCode;
import org.biiig.dmgm.impl.operators.CollectionOperatorBase;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SubgraphMiningBase extends CollectionOperatorBase {
  private static final GroupByKeyListValues<DFSCodeEmbeddingPair, DFSCode, DFSEmbedding> AGGREGATION = new GroupByKeyListValues<>(DFSCodeEmbeddingPair::getDfsCode, DFSCodeEmbeddingPair::getEmbedding);

  private static final Predicate<DFSCode> IS_MINIMAL = new IsMinimal();
  private static final Predicate<Map.Entry<DFSCode, List<DFSEmbedding>>> VERIFICATION = e -> IS_MINIMAL.test(e.getKey());
  private static final Function<Map.Entry<DFSCode,List<DFSEmbedding>>, DFSCodeEmbeddingsPair> ADD_SUPPORT = new AddSupport();
  private static final String FSG_LABEL = "Frequent Subgraph";
  private static final String FSGS_LABEL = "Frequent Subgraphs";

  protected final float minSupport;
  private final Predicate<Map.Entry<DFSCode, List<DFSEmbedding>>> edgeCountLimit;

  public SubgraphMiningBase(float minSupport, int maxEdgeCount) {
    this.minSupport = minSupport;
    edgeCountLimit = e -> e.getKey().getEdgeCount() < maxEdgeCount;
  }

  // ORCHESTRATION
  @Override
  public Long apply(GraphDB database, Long collectionId) {
    int supportKey = database.encode(PropertyKeys.SUPPORT);
    int frequencyKey = database.encode(PropertyKeys.FREQUENCY);
    int dfsCodeKey = database.encode(PropertyKeys.DFS_CODE);
    int patternLabel = database.encode(FSG_LABEL);
    int outputCollectionLabel = database.encode(FSG_LABEL);

    Map<DFSCode, BiConsumer<GraphDB, Long>> output = Maps.newConcurrentMap();

    Function<CachedGraph, Stream<DFSCodeEmbeddingPair>> initializeParents = new InitializeParents(patternLabel);

    Consumer<DFSCodeEmbeddingsPair> store =
      s -> output.put(s.getDFSCode(), (db, gid) -> {
        db.set(gid, supportKey, Math.toIntExact(s.getSupport()));
        db.set(gid, frequencyKey, s.getFrequency());
      });

    Map<Long, CachedGraph> input = database
      .getCachedCollection(collectionId)
      .stream()
      .collect(Collectors.toMap(CachedGraph::getId, Function.identity()));

    input = preprocess(input);

    long minSupportAbsolute = Math.round(input.size() * minSupport);

    Predicate<DFSCodeEmbeddingsPair> patternPredicate = s -> s.getSupport() >= minSupportAbsolute;
    GrowAllChildren growAllChildren = new GrowAllChildren(input);


    Stream<Map.Entry<DFSCode, List<DFSEmbedding>>> candidateStream = input
      .values()
      .stream()
      .flatMap(initializeParents)
      .collect(AGGREGATION)
      .entrySet()
      .stream();

    Stream<DFSCodeEmbeddingsPair> parentStream = filterAndOutput(store, patternPredicate, candidateStream);

    List<DFSCodeEmbeddingsPair> parents = parentStream
      .collect(Collectors.toList());

    while (!parents.isEmpty()) {
      parents = filterAndOutput(store, patternPredicate, parents
        .stream()
        .flatMap(growAllChildren)
        .collect(AGGREGATION)
        .entrySet()
        .stream()
        .filter(VERIFICATION)
        .filter(edgeCountLimit))
        .collect(Collectors.toList());
    }

    long[] outputVertices = output
      .entrySet()
      .stream()
      .mapToLong(entry -> {
        DFSCode dfsCode = entry.getKey();
        long id = createGraph(database, dfsCode);
        database.set(id, dfsCodeKey, dfsCode.toString(database));
        entry.getValue().accept(database, id);
        return id;
      })
      .toArray();


    return database.createGraph(outputCollectionLabel, outputVertices, new long[0]);
  }

  public Stream<DFSCodeEmbeddingsPair> filterAndOutput(Consumer<DFSCodeEmbeddingsPair> store, Predicate<DFSCodeEmbeddingsPair> patternPredicate, Stream<Map.Entry<DFSCode, List<DFSEmbedding>>> candidateStream) {
    return candidateStream
      .map(ADD_SUPPORT)
      .filter(patternPredicate)
      .peek(store);
  }

  protected abstract Map<Long,CachedGraph> preprocess(Map<Long, CachedGraph> input);
}
