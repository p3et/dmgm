package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import de.jesemann.paralleasy.queue_stream.QueueStreamSource;
import org.biiig.dmgm.api.ElementDataStore;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutputBase;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPropertyKeys;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneralizedFrequent extends FilterAndOutputBase {
  private final ElementDataStore dataStore;

  GeneralizedFrequent(
    int minSupportAbs, GraphCollection output, ElementDataStore dataStore) {
    super(output, minSupportAbs);
    this.dataStore = dataStore;
  }

  @Override
  protected boolean outputIfInteresting(DFSCodeEmbeddingsPair topLevelPairs, int topLevelFrequency) {
    boolean topLevelFrequent;
    int topLevelSupport = Math.toIntExact(
      Stream.of(topLevelPairs.getEmbeddings())
        .map(DFSEmbedding::getGraphId)
        .distinct()
        .count()
    );

    topLevelFrequent = topLevelSupport >= minSupportAbs;

    if (topLevelFrequent) {
      outputFrequentSpecializations(topLevelPairs);
      output(topLevelPairs.getDfsCode(), topLevelSupport, topLevelFrequency);
    }
    return topLevelFrequent;
  }

  private void outputFrequentSpecializations(DFSCodeEmbeddingsPair topLevelPairs) {
    DFSCode topLevel = topLevelPairs.getDfsCode();

    List<MultiDimensionalVector> topLevelParents = Stream.of(topLevelPairs.getEmbeddings())
      .map(new ToMultiDimensionalVector(dataStore))
      .collect(Collectors.toList());

    QueueStreamSource<MultiDimensionalVector> queue = QueueStreamSource.of(topLevelParents);

    queue
      .stream()
      .flatMap(new AllSpecializations())
      .collect(Collectors.groupingBy(Function.identity(), Collectors.toList()))
      .entrySet()
      .stream()
      .filter(e -> e.getValue().size() > minSupportAbs)
      // add only one representative to output
      .peek(new OutputSpecialization(topLevel))
      .flatMap(e -> e.getValue().stream())
      .forEach(queue::add);
  }

  private void output(DFSCode dfsCode, int support, int frequency) {
    int graphId = output.add(dfsCode);
    output.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.SUPPORT, support);
    output.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.FREQUENCY, frequency);
  }

}
