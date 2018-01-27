package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import com.google.common.collect.Maps;
import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import org.biiig.dmgm.api.HyperVertexDB;
import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.impl.graph.DFSCode;
import org.biiig.dmgm.impl.operators.HyperVertexOperatorBase;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SubgraphMiningBase extends HyperVertexOperatorBase {
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
  public Long apply(HyperVertexDB database, Long collectionId) {
    int supportKey = database.encode(SubgraphMiningPropertyKeys.SUPPORT);
    int frequencyKey = database.encode(SubgraphMiningPropertyKeys.FREQUENCY);
    int dfsCodeKey = database.encode(SubgraphMiningPropertyKeys.DFS_CODE);
    int patternLabel = database.encode(FSG_LABEL);
    int outputCollectionLabel = database.encode(FSG_LABEL);

    Map<DFSCode, BiConsumer<HyperVertexDB, Long>> output = Maps.newConcurrentMap();

    Function<SmallGraph, Stream<DFSCodeEmbeddingPair>> initializeParents = new InitializeParents(patternLabel);
    
    Consumer<DFSCodeEmbeddingsPair> store =
      s -> output.put(s.getDFSCode(), (db, gid) -> {
        db.set(gid, supportKey, s.getSupport());
        db.set(gid, frequencyKey, s.getFrequency());
      });
    
    Map<Long, SmallGraph> input = database
      .getCollection(collectionId)
      .stream()
      .collect(Collectors.toMap(SmallGraph::getId, Function.identity()));

    long minSupportAbsolute = Math.round(input.size() * minSupport);

    Predicate<DFSCodeEmbeddingsPair> patternPredicate = s -> s.getSupport() >= minSupportAbsolute;
    GrowAllChildren growAllChildren = new GrowAllChildren(input);


    List<DFSCodeEmbeddingsPair> parents = input
      .values()
      .stream()
      .flatMap(initializeParents)
      .collect(AGGREGATION)
      .entrySet()
      .stream()
      .map(ADD_SUPPORT)
      .filter(patternPredicate)
      .peek(store)
      .collect(Collectors.toList());

    while (!parents.isEmpty()) {
      parents = parents
        .stream()
        .flatMap(growAllChildren)
        .collect(AGGREGATION)
        .entrySet()
        .stream()
        .filter(VERIFICATION)
        .filter(edgeCountLimit)
        .map(ADD_SUPPORT)
        .filter(patternPredicate)
        .peek(store)
        .collect(Collectors.toList());
    }

    long[] outputVertices = output
      .entrySet()
      .stream()
      .mapToLong(entry -> {
        DFSCode dfsCode = entry.getKey();
        long id = database.createHyperVertex(dfsCode);
        database.set(id, dfsCodeKey, dfsCode.toString(database));
        entry.getValue().accept(database, id);
        return id;
      })
      .toArray();


    return database.createHyperVertex(outputCollectionLabel, outputVertices, new long[0]);
  }

  protected abstract Preprocessor getPreprocessor();

  protected abstract FilterOrOutput<DFSCodeEmbeddingsPair> getFilterAndOutput(List<SmallGraph> rawInput, HyperVertexDB db);


}
