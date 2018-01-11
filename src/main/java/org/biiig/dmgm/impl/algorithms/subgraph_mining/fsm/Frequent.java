package org.biiig.dmgm.impl.algorithms.subgraph_mining.fsm;

import javafx.util.Pair;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPropertyKeys;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.Supportable;

import java.util.Optional;
import java.util.function.Consumer;

public class Frequent<T extends Supportable> implements FilterOrOutput<T> {

  private final int minSupportAbsolute;

  public Frequent(int minSupportAbsolute) {
    this.minSupportAbsolute = minSupportAbsolute;
  }

  @Override
  public Pair<Optional<T>, Optional<Consumer<GraphCollection>>> apply(T supportable) {    int embeddingCount = supportable.getEmbeddingCount();
    int support = supportable.getSupport();

    boolean frequent = support >= minSupportAbsolute;

    Optional<T> child;
    Optional<Consumer<GraphCollection>> store;

    if (frequent) {
      child = Optional.of(supportable);
      store = Optional.of(s -> {
        int graphId = s.add(supportable.getPattern());
        s.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.SUPPORT, support);
        s.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.EMBEDDING_COUNT, embeddingCount);
      });

    } else {
      child = Optional.empty();
      store = Optional.empty();
    }


    return new Pair<>(child, store);
  }
}
