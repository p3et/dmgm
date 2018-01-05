package org.biiig.dmgm.impl.algorithms.subgraph_mining.fsm;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutputBase;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPropertyKeys;
import org.biiig.dmgm.impl.graph.DFSCode;

public class Frequent extends FilterAndOutputBase {

  private final int minSupportAbsolute;

  protected Frequent(GraphCollection output, int minSupportAbsolute) {
    super(output);
    this.minSupportAbsolute = minSupportAbsolute;
  }

  @Override
  public boolean test(DFSCodeEmbeddingsPair pairs) {
    DFSEmbedding[] embeddings = pairs.getEmbeddings();

    int frequency = embeddings.length;
    int support = frequency >= minSupportAbsolute ? getSupport(embeddings) : 0;

    boolean frequent = support >= minSupportAbsolute;
    if (frequent)
      store(pairs.getDfsCode(), frequency, support);

    return frequent;
  }

  private void store(DFSCode dfsCode, int frequency, int support) {
    int graphId = output.add(dfsCode);
    output.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.SUPPORT, support);
    output.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.EMBEDDING_COUNT, frequency);
  }
}
