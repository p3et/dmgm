package org.biiig.dmgm.impl.operators.subgraph_mining.frequent;

import javafx.util.Pair;
import org.biiig.dmgm.api.HyperVertexDB;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.Supportable;

import java.util.Optional;
import java.util.function.Consumer;

public class FrequentFilter<T extends Supportable> implements FilterOrOutput<T> {

  private final int minSupportAbsolute;

  public FrequentFilter(int minSupportAbsolute) {
    this.minSupportAbsolute = minSupportAbsolute;
  }

  @Override
  public Pair<Optional<T>, Optional<Consumer<HyperVertexDB>>> apply(T supportable) {    int embeddingCount = supportable.getFrequency();
    long support = supportable.getSupport();

    boolean frequent = support >= minSupportAbsolute;

    Optional<T> child;
    Optional<Consumer<HyperVertexDB>> store;

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
