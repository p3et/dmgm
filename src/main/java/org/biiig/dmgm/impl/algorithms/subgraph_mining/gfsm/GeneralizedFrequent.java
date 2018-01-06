package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.fsm.Frequent;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneralizedFrequent extends Frequent {


  GeneralizedFrequent(GraphCollection output, int minSupportAbsolute) {
    super(output, minSupportAbsolute);
  }

  @Override
  public boolean test(DFSCodeEmbeddingsPair pairs) {
    outputFrequentSpecializations(pairs);

    return super.test(pairs);
  }

  private void outputFrequentSpecializations(DFSCodeEmbeddingsPair topLevelPairs) {
    DFSCode topLevel = topLevelPairs.getDfsCode();

    List<MultiDimensionalVector> vectors = Stream.of(topLevelPairs.getEmbeddings())
        .map(new ToMultiDimensionalVector(output.getElementDataStore()))
        .collect(Collectors.toList());

    while (!vectors.isEmpty())
      vectors = vectors
        .stream()
        .flatMap(new AllSpecializations(topLevel.getVertexCount()))
        .collect(Collectors.groupingBy(Function.identity(), Collectors.toList()))
        .entrySet()
        .stream()
        .filter(new FrequentSpecialization(output, minSupportAbsolute, topLevel))
        // but add all instances to queue
        .flatMap(e -> e.getValue().stream())
        .collect(Collectors.toList());
  }


}
