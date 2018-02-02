package org.biiig.dmgm.impl.operators.subgraph_mining.frequent;

import javafx.util.Pair;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeSupportablePair;

import java.util.Optional;
import java.util.function.Consumer;

public class FrequentFilter<T extends DFSCodeSupportablePair> implements FilterOrOutput<T> {

  private final int minSupportAbsolute;

  public FrequentFilter(int minSupportAbsolute) {
    this.minSupportAbsolute = minSupportAbsolute;
  }

  @Override
  public Pair<Optional<T>, Optional<Consumer<GraphDB>>> apply(T supportable) {    int embeddingCount = 0;
    long support =0l;

    boolean frequent = support >= minSupportAbsolute;

    Optional<T> child;
    Optional<Consumer<GraphDB>> store;

    if (frequent) {
      child = Optional.of(supportable);
      store = Optional.of(s -> {
//        int graphId = s.add(supportable.getDFSCode());
//        s.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.SUPPORT, support);
//        s.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.FREQUENCY, embeddingCount);
      });

    } else {
      child = Optional.empty();
      store = Optional.empty();
    }


    return new Pair<>(child, store);
  }
}
