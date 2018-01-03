package org.biiig.dmgm.impl.algorithms.subgraph_mining.fsm;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutput;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPropertyKeys;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.stream.Stream;

public class Frequent implements FilterAndOutput {
  private final int minSupportAbs;
  private final GraphCollection output;

  public Frequent(int minSupportAbs, GraphCollection output) {
    this.minSupportAbs = minSupportAbs;
    this.output = output;
  }

  @Override
  public boolean test(DFSCodeEmbeddingsPair pairs) {
    DFSEmbedding[] embeddings = pairs.getEmbeddings();
    int frequency = embeddings.length;

    boolean frequent = frequency >= minSupportAbs;

    if (frequent) {
      int support = Math.toIntExact(
        Stream.of(embeddings)
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
    }

    return frequent;
  }


}
