package org.biiig.dmgm.impl.operators.subgraph_mining.frequent;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.Preprocessor;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.Supportable;

public interface Frequent {
  default Preprocessor getFrequentLabels(float minSupport) {
    return new FrequentLabels(minSupport);
  }

  default <T extends Supportable> FilterOrOutput<T> getFilterOrOutput(GraphCollection rawInput, float minSupport) {
    return new FrequentFilter<>(Math.round(minSupport * rawInput.size()));
  }
}
