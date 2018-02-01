package org.biiig.dmgm.impl.operators.subgraph_mining;

import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.graph.DFSCode;
import org.biiig.dmgm.impl.operators.subgraph.FilterVerticesAndEdgesByLabel;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.frequent.AddTotalSupport;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.frequent.Frequent;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Directed Multigraph gSpan
 */
public class FrequentSubgraphs extends SubgraphMiningBase implements Frequent {

  private long minSupportAbsolute;


  public FrequentSubgraphs(GraphDB database, float minSupport, int maxEdgeCount) {
    super(database, minSupport, maxEdgeCount);
  }

  @Override
  protected Map<Long, CachedGraph> preProcess(List<CachedGraph> input) {
    minSupportAbsolute = Math.round(input.size() * minSupport);

    Set<Integer> frequentVertexLabels = getFrequentLabels(input, CachedGraph::getVertexLabels);

    input = input
      .stream()
      .map(new FilterVerticesAndEdgesByLabel(frequentVertexLabels::contains, e-> true, true))
      .collect(Collectors.toList());

    Set<Integer> frequentEdgeLabels = getFrequentLabels(input, CachedGraph::getEdgeLabels) ;

    return input
      .stream()
      .map(new FilterVerticesAndEdgesByLabel(v -> true, frequentEdgeLabels::contains, true))
      .collect(Collectors.toMap(CachedGraph::getId, Function.identity()));
  }

  private Set<Integer> getFrequentLabels(List<CachedGraph> graphs, Function<CachedGraph, int[]> labelSelector) {
    return graphs
      .stream()
      .flatMap(g -> IntStream
        .of(labelSelector.apply(g))
        .distinct()
        .boxed())
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
      .entrySet()
      .stream()
      .filter(e -> e.getValue() >= minSupportAbsolute)
      .map(Map.Entry::getKey)
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

    return candidateStream
      .map(new AddTotalSupport())
      .filter(p -> p.getSupport() >= minSupportAbsolute)
      .peek(s -> output.put(s.getDFSCode(), (db, gid) -> {
        db.set(gid, supportKey, Math.toIntExact(s.getSupport()));
        db.set(gid, frequencyKey, s.getFrequency());
      }))
      .collect(Collectors.toList());
  }
}
