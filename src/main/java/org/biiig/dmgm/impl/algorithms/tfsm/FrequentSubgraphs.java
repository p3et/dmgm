package org.biiig.dmgm.impl.algorithms.tfsm;

import de.jesemann.queue_stream.QueueStreamSource;
import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.api.model.collection.GraphCollection;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.model.collection.InMemoryGraphCollection;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Directed Multigraph gSpan
 */
public class FrequentSubgraphs implements Operator {

  private final float minSupportRel;
  private final int maxEdgeCount;

  public FrequentSubgraphs(float minSupportRel, int maxEdgeCount) {
    this.minSupportRel = minSupportRel;
    this.maxEdgeCount = maxEdgeCount;
  }


  @Override
  public GraphCollection apply(GraphCollection inputCollection) {
    GraphCollection result = new InMemoryGraphCollection()
      .withVertexDictionary(inputCollection.getVertexDictionary())
      .withEdgeDictionary(inputCollection.getEdgeDictionary());

    int minSupportAbs = Math.round((float) inputCollection.size() * this.minSupportRel);

    GraphCollection prunedCollection = pruneByLabels(inputCollection, minSupportAbs);

    List<DFSCodeEmbeddingsPair> parents = prunedCollection
      .parallelStream()
      .flatMap(new InitializeParents())
      .collect(new GroupByDFSCodeArrayEmbeddings())
      .entrySet()
      .parallelStream()
      .map(e -> new DFSCodeEmbeddingsPair(e.getKey(), e.getValue()))
      .filter(new FilterFrequent(minSupportAbs))
      .collect(Collectors.toList());

    QueueStreamSource<DFSCodeEmbeddingsPair> queueStreamSource = QueueStreamSource.of(parents);

    queueStreamSource
      .parallelStream()
      .forEach(new OutputAndGrowChildren(prunedCollection, result, queueStreamSource, minSupportAbs));

    return result;
  }

  private GraphCollection pruneByLabels(GraphCollection inputCollection, int minSupportAbs) {
    Set<Integer> frequentVertexLabels = getFrequentLabels(
      inputCollection
        .parallelStream()
        .flatMap(new DistinctVertexLabels()),
      minSupportAbs);

    GraphCollection vertexPrunedCollection = new InMemoryGraphCollection();

    inputCollection
      .parallelStream()
      .map(new PruneVertices(frequentVertexLabels))
      .forEach(vertexPrunedCollection::add);

    Set<Integer> frequentEdgeLabels = getFrequentLabels(
      inputCollection
        .parallelStream()
        .flatMap(new DistinctEdgeLabels()),
      minSupportAbs);

    GraphCollection prunedCollection = new InMemoryGraphCollection()
      .withVertexDictionary(inputCollection.getVertexDictionary())
      .withEdgeDictionary(inputCollection.getEdgeDictionary());

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
