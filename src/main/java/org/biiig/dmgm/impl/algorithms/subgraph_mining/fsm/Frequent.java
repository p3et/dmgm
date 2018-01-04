package org.biiig.dmgm.impl.algorithms.subgraph_mining.fsm;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutputBase;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPropertyKeys;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.stream.Stream;

public class Frequent extends FilterAndOutputBase {

  Frequent(int minSupportAbs, GraphCollection output) {
    super(output, minSupportAbs);
  }

  @Override
  protected boolean outputIfInteresting(DFSCodeEmbeddingsPair pairs, int frequency) {
    boolean frequent;
    int support = Math.toIntExact(
      Stream.of(pairs.getEmbeddings())
        .map(DFSEmbedding::getGraphId)
        .distinct()
        .count()
    );

    frequent = support >= minSupportAbs;

    if (frequent) {
      DFSCode dfsCode = pairs.getDfsCode();
      int graphId = output.add(dfsCode);
      output.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.SUPPORT, support);
      output.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.FREQUENCY, frequency);
    }
    return frequent;
  }

}
