package org.biiig.dmgm.impl.algorithms.subgraph_mining.common;

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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SubgraphMiningBase implements Operator {
  private final GrowAllChildren growAllChildren = new GrowAllChildren();

  private final float minSupportRel;
  protected final int maxEdgeCount;

  public SubgraphMiningBase(float minSupportRel, int maxEdgeCount) {
    this.maxEdgeCount = maxEdgeCount;
    this.minSupportRel = minSupportRel;
  }

  // ORCHESTRATION

  @Override
  public GraphCollection apply(GraphCollection rawInput) {

    int minSupportAbs = Math.round((float) rawInput.size() * this.minSupportRel);

    GraphCollectionBuilder collectionBuilder = new InMemoryGraphCollectionBuilderFactory()
      .create()
      .withLabelDictionary(rawInput.getLabelDictionary())
      .withElementDataStore(rawInput.getElementDataStore());

    GraphCollection input = pruneByLabels(rawInput, minSupportAbs, collectionBuilder);
    GraphCollection output = collectionBuilder.create();

    Map<DFSCode, DFSEmbedding[]> singleEdgeParents = initializeSingle(input);

    FilterAndOutputFactory filterAndOutputFactory = getFilterAndOutputFactory(input);
    FilterAndOutput filterAndOutput = filterAndOutputFactory.create(minSupportAbs, output);
    List<DFSCodeEmbeddingsPair> parents = aggregateSingle(singleEdgeParents, filterAndOutput);

    QueueStreamSource<DFSCodeEmbeddingsPair> queue = QueueStreamSource.of(parents);
    queue
      .parallelStream()
      .forEach(parent -> {
        Stream<DFSCodeEmbeddingPair> children = growChildren(parent, input);
        aggregateChildren(children, filterAndOutput, queue);
      });

    return output;
  }

  public abstract FilterAndOutputFactory getFilterAndOutputFactory(GraphCollection input);

  // MINING

  private Map<DFSCode, DFSEmbedding[]> initializeSingle(GraphCollection input) {
    return input
      .parallelStream()
      .flatMap(new InitializeParents())
      .collect(new GroupByDFSCodeArrayEmbeddings());
  }

  private List<DFSCodeEmbeddingsPair> aggregateSingle(
    Map<DFSCode, DFSEmbedding[]> reports, FilterAndOutput filterAndOutput) {

    return reports
        .entrySet()
        .parallelStream()
        .map(e -> new DFSCodeEmbeddingsPair(e.getKey(), e.getValue()))
        .filter(filterAndOutput)
        .collect(Collectors.toList());
  }

  private Stream<DFSCodeEmbeddingPair>  growChildren(DFSCodeEmbeddingsPair parents, GraphCollection input) {

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
      );
  }

  private void aggregateChildren(
    Stream<DFSCodeEmbeddingPair> children,
    FilterAndOutput filterAndOutput,
    QueueStreamSource<DFSCodeEmbeddingsPair> queue) {

    children
      .collect(new GroupByDFSCodeArrayEmbeddings())
      .entrySet()
      .stream()
      .map(e -> new DFSCodeEmbeddingsPair(e.getKey(), e.getValue()))
      .filter(new IsMinimal())
      .filter(filterAndOutput)
      .filter(p -> p.getDfsCode().getEdgeCount() < maxEdgeCount)
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
