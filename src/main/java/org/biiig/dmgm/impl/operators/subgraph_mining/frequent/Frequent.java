package org.biiig.dmgm.impl.operators.subgraph_mining.frequent;

import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.Preprocessor;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeSupportablePair;

import java.util.List;

public interface Frequent {
  default Preprocessor getFrequentLabels(float minSupport) {
    return new FrequentLabels(minSupport);
  }

  default <T extends DFSCodeSupportablePair> FilterOrOutput<T> getFilterOrOutput(List<CachedGraph> rawInput, float minSupport) {
    return new FrequentFilter<>(Math.round(minSupport * rawInput.size()));
  }
}
