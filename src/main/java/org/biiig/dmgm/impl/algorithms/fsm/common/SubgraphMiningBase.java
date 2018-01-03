package org.biiig.dmgm.impl.algorithms.fsm.common;

import de.jesemann.paralleasy.queue_stream.QueueStreamSource;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.GraphCollectionBuilder;
import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.algorithms.fsm.fsm.*;
import org.biiig.dmgm.impl.graph.DFSCode;
import org.biiig.dmgm.impl.graph_collection.InMemoryGraphCollectionBuilderFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SubgraphMiningBase implements Operator {
  private final GrowAllChildren growAllChildren = new GrowAllChildren();


  protected final float minSupportRel;
  protected final int maxEdgeCount;

  public SubgraphMiningBase(float minSupportRel, int maxEdgeCount) {
    this.maxEdgeCount = maxEdgeCount;
    this.minSupportRel = minSupportRel;
  }

  // MINING

  protected Map<DFSCode, DFSEmbedding[]> initializeSingle(GraphCollection input) {
    return input
      .parallelStream()
      .flatMap(new InitializeParents())
      .collect(new GroupByDFSCodeArrayEmbeddings());
  }

  protected List<DFSCodeEmbeddingsPair> aggregateSingle(
    Map<DFSCode, DFSEmbedding[]> reports, Predicate<DFSCodeEmbeddingsPair> predicate, GraphCollection result) {

    return reports
        .entrySet()
        .parallelStream()
        .map(e -> new DFSCodeEmbeddingsPair(e.getKey(), e.getValue()))
        .filter(predicate)
        .peek(p -> result.add(p.getDfsCode()))
        .collect(Collectors.toList());
  }

  protected Map<DFSCode, DFSEmbedding[]> growChildren(DFSCodeEmbeddingsPair parents, GraphCollection input) {

    DFSCode parentCode = parents.getDfsCode();
    int[] rightmostPath = parentCode.getRightmostPath();

    return Stream.of(parents.getEmbeddings())
      .collect(new GroupByGraphIdArrayEmbeddings())
      .entrySet()
      .stream()
      .flatMap(
        entry -> {
          Graph graph = input.getGraph(entry.getKey());
          return Stream.of(entry.getValue())
            .flatMap(
              embedding ->
                Stream.of(growAllChildren.apply(graph, parentCode, rightmostPath, embedding)));
        }
      )
      .collect(new GroupByDFSCodeArrayEmbeddings());
  }

  protected void aggregateChildren(
    Map<DFSCode, DFSEmbedding[]> children,
    Predicate<DFSCodeEmbeddingsPair> predicate,
    QueueStreamSource<DFSCodeEmbeddingsPair> queue,
    GraphCollection output) {

    children
      .entrySet()
      .stream()
      .map(e -> new DFSCodeEmbeddingsPair(e.getKey(), e.getValue()))
      .filter(new IsMinimal())
      .filter(predicate)
      .forEach(p -> {
        queue.add(p);
        output.add(p.getDfsCode());
      });
  }

  // PREPROCESSING

  protected GraphCollection pruneByLabels(
    GraphCollection inputCollection, int minSupportAbs, GraphCollectionBuilder collectionBuilder) {

    Set<Integer> frequentVertexLabels = getFrequentLabels(
      inputCollection
        .parallelStream()
        .flatMap(new DistinctVertexLabels()),
      minSupportAbs);

    GraphCollection vertexPrunedCollection = collectionBuilder.create();

    inputCollection
      .parallelStream()
      .map(new PruneVertices(frequentVertexLabels))
      .forEach(vertexPrunedCollection::add);

    Set<Integer> frequentEdgeLabels = getFrequentLabels(
      inputCollection
        .parallelStream()
        .flatMap(new DistinctEdgeLabels()),
      minSupportAbs);

    GraphCollection prunedCollection = collectionBuilder.create();

    vertexPrunedCollection
      .parallelStream()
      .map(new PruneEdges(frequentEdgeLabels))
      .forEach(prunedCollection::add);

    return vertexPrunedCollection;
  }

  private Set<Integer> getFrequentLabels(Stream<Integer> vertexLabels, int minSupportAbs) {
    return vertexLabels
      .collect(Collectors.groupingByConcurrent(Function.identity(), Collectors.counting()))
      .entrySet()
      .stream()
      .filter(e -> e.getValue() >= minSupportAbs)
      .map(Map.Entry::getKey)
      .collect(Collectors.toSet());
  }
}
