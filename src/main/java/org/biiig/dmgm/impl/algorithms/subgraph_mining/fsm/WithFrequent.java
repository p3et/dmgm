package org.biiig.dmgm.impl.algorithms.subgraph_mining.fsm;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.Supportable;

public interface WithFrequent {

  default <T extends Supportable> FilterOrOutput<T> getFrequent(GraphCollection rawInput, float minSupport) {
    return new Frequent<>(Math.round(minSupport * rawInput.size()));
  }

}
