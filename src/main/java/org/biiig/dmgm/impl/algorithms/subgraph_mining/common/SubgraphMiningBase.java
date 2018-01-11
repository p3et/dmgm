package org.biiig.dmgm.impl.algorithms.subgraph_mining.common;

import de.jesemann.paralleasy.recursion.RecursiveTask;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.GraphCollectionBuilder;
import org.biiig.dmgm.impl.graph_collection.InMemoryGraphCollectionBuilderFactory;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
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

    FilterOrOutput<DFSCodeEmbeddingsPair> filterOrOutput = getFilterAndOutput(rawInput);


    GraphCollection input = pruneByLabels(rawInput, collectionBuilder);

    SingleEdgeDFSNodes singleEdgeDFSNodes = new SingleEdgeDFSNodes(input, filterOrOutput);
    singleEdgeDFSNodes.run();

    RecursiveTask<DFSCodeEmbeddingsPair, Consumer<GraphCollection>> patternGrowth = RecursiveTask
      .createFor(new ProcessDFSNode(input, filterOrOutput, maxEdgeCount))
      .on(singleEdgeDFSNodes.getSingleEdgeDFSNodes());
    patternGrowth.run();

    GraphCollection output = collectionBuilder.create();

    singleEdgeDFSNodes
      .getOutput()
      .forEach(c -> c.accept(output));

    patternGrowth
      .getOutput()
      .forEach(c -> c.accept(output));

    return output;
  }

  protected abstract FilterOrOutput<DFSCodeEmbeddingsPair> getFilterAndOutput(GraphCollection rawInput);


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
