package org.biiig.dmgm.impl.algorithms.subgraph_mining.common;

import de.jesemann.paralleasy.recursion.RecursiveTask;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.GraphCollectionBuilder;
import org.biiig.dmgm.impl.graph.DFSCode;
import org.biiig.dmgm.impl.graph_collection.InMemoryGraphCollectionBuilderFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SubgraphMiningBase extends org.biiig.dmgm.impl.algorithms.OperatorBase {
  protected final float minSupport;
  private final int maxEdgeCount;

  public SubgraphMiningBase(float minSupport, int maxEdgeCount) {
    this.minSupport = minSupport;
    this.maxEdgeCount = maxEdgeCount;
  }

  // ORCHESTRATION

  @Override
  public GraphCollection apply(GraphCollection rawInput) {
    GraphCollectionBuilder collectionBuilder = new InMemoryGraphCollectionBuilderFactory()
      .create()
      .withLabelDictionary(rawInput.getLabelDictionary())
      .withElementDataStore(rawInput.getElementDataStore());

    FilterAndOutputFactory filterAndOutputFactory = getFilterAndOutputFactory(rawInput);

    GraphCollection input = pruneByLabels(rawInput, collectionBuilder);
    GraphCollection output = collectionBuilder.create();

    Map<DFSCode, DFSEmbedding[]> singleEdgeParents = initializeSingle(input);
    FilterAndOutput filterAndOutput = filterAndOutputFactory.create(output);


    List<DFSCodeEmbeddingsPair> parents = aggregateSingle(singleEdgeParents, filterAndOutput);

    RecursiveTask<DFSCodeEmbeddingsPair, Graph> queue = RecursiveTask
      .createFor(new ProcessDFSNode(input, filterAndOutput, maxEdgeCount))
      .on(parents);

    queue.run();


    return output;
  }

  public abstract FilterAndOutputFactory getFilterAndOutputFactory(GraphCollection input);

  // MINING

  private Map<DFSCode, DFSEmbedding[]> initializeSingle(GraphCollection input) {
    return input
      .stream()
      .flatMap(new InitializeParents())
      .collect(new GroupByDFSCodeArrayEmbeddings());
  }

  private List<DFSCodeEmbeddingsPair> aggregateSingle(
    Map<DFSCode, DFSEmbedding[]> reports, FilterAndOutput filterAndOutput) {

    return reports
        .entrySet()
        .stream()
        .map(e -> new DFSCodeEmbeddingsPair(e.getKey(), e.getValue()))
        .filter(filterAndOutput)
        .collect(Collectors.toList());
  }

  // PREPROCESSING

  protected GraphCollection pruneByLabels(
    GraphCollection inputCollection, GraphCollectionBuilder collectionBuilder) {

    Integer minSupportAbsolute = Math.round(inputCollection.size() * minSupport);

    Set<Integer> frequentVertexLabels = getFrequentLabels(
      inputCollection
        .stream()
        .flatMap(new DistinctVertexLabels()),
      minSupportAbsolute);

    GraphCollection vertexPrunedCollection = collectionBuilder.create();

    inputCollection
      .stream()
      .map(new PruneVertices(frequentVertexLabels))
      .forEach(vertexPrunedCollection::add);

    Set<Integer> frequentEdgeLabels = getFrequentLabels(
      inputCollection
        .stream()
        .flatMap(new DistinctEdgeLabels()),
      minSupportAbsolute);

    GraphCollection prunedCollection = collectionBuilder.create();

    vertexPrunedCollection
      .stream()
      .map(new PruneEdges(frequentEdgeLabels))
      .forEach(prunedCollection::add);

    return vertexPrunedCollection;
  }

  private Set<Integer> getFrequentLabels(Stream<Integer> labels, Integer minSupportAbsolute) {
    return labels
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
      .entrySet()
      .stream()
      .filter(e -> e.getValue() >= minSupportAbsolute)
      .map(Map.Entry::getKey)
      .collect(Collectors.toSet());
  }

}
