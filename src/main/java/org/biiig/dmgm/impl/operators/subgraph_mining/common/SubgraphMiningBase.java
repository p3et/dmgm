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
  public static final Function<SmallGraph, Stream<DFSCodeEmbeddingPair>> INITIALIZE_PARENTS = new InitializeParents();
  public static final Predicate<DFSCode> IS_MINIMAL = new IsMinimal();
  private static final Function<Map.Entry<DFSCode,List<DFSEmbedding>>, DFSCodeEmbeddingsPair> ADD_SUPPORT = new AddSupport();
  private static final String FSG_LABEL = "Frequent Subgraph";
  private static final String FSGS_LABEL = "Frequent Subgraphs";

  protected final float minSupport;
  private final int maxEdgeCount;

  public SubgraphMiningBase(float minSupport, int maxEdgeCount) {
    this.minSupport = minSupport;
    this.maxEdgeCount = maxEdgeCount;
  }

  // ORCHESTRATION
  @Override
  public Long apply(HyperVertexDB database, Long collectionId) {
    int supportKey = database.encode(SubgraphMiningPropertyKeys.SUPPORT);
    int dfsCodeKey = database.encode(SubgraphMiningPropertyKeys.DFS_CODE);
    int patternLabel = database.encode(FSG_LABEL);
    int patternsLabel = database.encode(FSG_LABEL);

    Map<DFSCode, BiConsumer<HyperVertexDB, Long>> output = Maps.newConcurrentMap();
    
    Consumer<DFSCodeEmbeddingsPair> store =
      s -> output.put(s.getDFSCode(), (db, gid) -> db.set(gid, supportKey, s.getSupport()));
    
    Map<Long, SmallGraph> input = database
      .getCollection(collectionId)
      .stream()
      .collect(Collectors.toMap(SmallGraph::getId, Function.identity()));

    long minSupportAbsolute = Math.round(input.size() * minSupport);

    Predicate<DFSCodeEmbeddingsPair> patternPredicate = s -> s.getSupport() >= minSupportAbsolute;
    
    List<DFSCodeEmbeddingsPair> parents = input
      .values()
      .stream()
      .flatMap(INITIALIZE_PARENTS)
      .collect(new GroupByKeyListValues<>(DFSCodeEmbeddingPair::getDfsCode, DFSCodeEmbeddingPair::getEmbedding))
      .entrySet()
      .stream()
      .map(ADD_SUPPORT)
      .filter(patternPredicate)
      .peek(store)
      .collect(Collectors.toList());

    GrowAllChildren growChildren = new GrowAllChildren();

    while (!parents.isEmpty())
      parents = parents
        .stream()
        .flatMap(p -> {
          DFSCode dfsCode = p.getDFSCode();
          int[] rightmostPath = dfsCode.getRightmostPath();

          return p.getEmbeddings()
            .stream()
            .flatMap(e -> Stream.of(growChildren.apply(input.get(e.getGraphId()), dfsCode, rightmostPath, e)));
        })
        .collect(new GroupByKeyListValues<>(DFSCodeEmbeddingPair::getDfsCode, DFSCodeEmbeddingPair::getEmbedding))
        .entrySet()
        .stream()
        .filter(e -> IS_MINIMAL.test(e.getKey()))
        .filter(e -> e.getKey().getEdgeCount() < maxEdgeCount)
        .map(ADD_SUPPORT)
        .filter(patternPredicate)
        .peek(store)
        .collect(Collectors.toList());

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


    return database.createHyperVertex(patternsLabel, outputVertices, new long[0]);
  }

  protected abstract Preprocessor getPreprocessor();

  protected abstract FilterOrOutput<DFSCodeEmbeddingsPair> getFilterAndOutput(List<SmallGraph> rawInput, HyperVertexDB db);


}
