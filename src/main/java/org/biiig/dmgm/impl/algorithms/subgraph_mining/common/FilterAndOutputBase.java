package org.biiig.dmgm.impl.algorithms.subgraph_mining.common;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutput;

public abstract class FilterAndOutputBase implements FilterAndOutput {
  protected final int minSupportAbs;
  protected final GraphCollection output;

  public FilterAndOutputBase(GraphCollection output, int minSupportAbs) {
    this.output = output;
    this.minSupportAbs = minSupportAbs;
  }

  @Override
  public boolean test(DFSCodeEmbeddingsPair pairs) {
    int frequency = pairs.getEmbeddings().length;
    return frequency >= minSupportAbs && outputIfInteresting(pairs, frequency);
  }

  protected abstract boolean outputIfInteresting(DFSCodeEmbeddingsPair pairs, int frequency);
}
