package org.biiig.dmgm.impl.algorithms.subgraph_mining.common;

import com.google.common.collect.Maps;
import de.jesemann.paralleasy.queue_stream.QueueStreamSource;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.GraphCollectionBuilder;
import org.biiig.dmgm.api.Operator;
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

  private final float minSupportRel;
  private final int maxEdgeCount;

  public SubgraphMiningBase(float minSupportRel, int maxEdgeCount) {
    this.maxEdgeCount = maxEdgeCount;
    this.minSupportRel = minSupportRel;
  }

  // ORCHESTRATION

  @Override
  public GraphCollection apply(GraphCollection rawInput) {
    FilterAndOutputFactory filterAndOutputFactory = getFilterAndOutputFactory(rawInput);

    int minSupportAbs = Math.round((float) rawInput.size() * this.minSupportRel);

    GraphCollectionBuilder collectionBuilder = new InMemoryGraphCollectionBuilderFactory()
      .create()
      .withLabelDictionary(rawInput.getLabelDictionary());

    GraphCollection input = pruneByLabels(rawInput, minSupportAbs, collectionBuilder);
    GraphCollection output = collectionBuilder.create();

    Map<DFSCode, DFSEmbedding[]> singleEdgeParents = initializeSingle(input);

    FilterAndOutput filterAndOutput = filterAndOutputFactory.create(minSupportAbs, output);
    List<DFSCodeEmbeddingsPair> parents = aggregateSingle(singleEdgeParents, filterAndOutput);

    QueueStreamSource<DFSCodeEmbeddingsPair> queue = QueueStreamSource.of(parents);

    queue
      .parallelStream()
      .forEach(parent -> {
        Map<DFSCode, DFSEmbedding[]> children = growChildren(parent, input);
        aggregateChildren(children, filterAndOutput, queue);
      });

    return output;
  }

  public abstract FilterAndOutputFactory getFilterAndOutputFactory(GraphCollection rawInput);

  // MINING

  private Map<DFSCode, DFSEmbedding[]> initializeSingle(GraphCollection input) {
    return input
      .parallelStream()
      .flatMap(new InitializeParents())
      .collect(new GroupByDFSCodeArrayEmbeddings());
  }

  private List<DFSCodeEmbeddingsPair> aggregateSingle(
    Map<DFSCode, DFSEmbedding[]> reports, FilterAndOutput predicate) {

    return reports
        .entrySet()
        .parallelStream()
        .map(e -> new DFSCodeEmbeddingsPair(e.getKey(), e.getValue()))
        .filter(predicate)
        .collect(Collectors.toList());
  }

  private Map<DFSCode, DFSEmbedding[]> growChildren(DFSCodeEmbeddingsPair parents, GraphCollection input) {

    DFSCode parentCode = parents.getDfsCode();
    Map<DFSCode, DFSEmbedding[]> children;

    if (parentCode.getEdgeCount() < maxEdgeCount) {
      int[] rightmostPath = parentCode.getRightmostPath();

      children = Stream.of(parents.getEmbeddings())
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
    } else {
      children = Maps.newHashMap();
    }

    return children;
  }

  private void aggregateChildren(
    Map<DFSCode, DFSEmbedding[]> children,
    Predicate<DFSCodeEmbeddingsPair> predicate,
    QueueStreamSource<DFSCodeEmbeddingsPair> queue) {

    children
      .entrySet()
      .stream()
      .map(e -> new DFSCodeEmbeddingsPair(e.getKey(), e.getValue()))
      .filter(new IsMinimal())
      .filter(predicate)
      .forEach(queue::add);
  }

  // PREPROCESSING

  private GraphCollection pruneByLabels(
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
        .flatMap(new DistinctEdgeLabels()), minSupportAbs);

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
