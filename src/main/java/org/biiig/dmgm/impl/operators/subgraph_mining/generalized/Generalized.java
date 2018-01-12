package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import javafx.util.Pair;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

public class Generalized implements FilterOrOutput<DFSCodeEmbeddingsPair> {


  private final Specializer specializer;
  private final FilterOrOutput<DFSCodeEmbeddingsPair> filter;

  public Generalized(FilterOrOutput<DFSCodeEmbeddingsPair> filter, Specializer specializer) {
    this.filter = filter;
    this.specializer = specializer;
  }

  @Override
  public Pair<Optional<DFSCodeEmbeddingsPair>, Optional<Consumer<GraphCollection>>> apply(DFSCodeEmbeddingsPair supportable) {
    Pair<Optional<DFSCodeEmbeddingsPair>, Optional<Consumer<GraphCollection>>> topLevel = filter.apply(supportable);

    Pair<Optional<DFSCodeEmbeddingsPair>, Optional<Consumer<GraphCollection>>> allLevels;

    if (topLevel.getValue().isPresent()) {
      // get store update for each specialization
      Collection<Consumer<GraphCollection>> outputs = specializer
        .apply(supportable);
      
      // add top level
      outputs.add(topLevel.getValue().get());
      
      // aggregate single consumer
      Consumer<GraphCollection> forEachOutput = c -> outputs.forEach(o -> o.accept(c));

      // update consumer
      allLevels = new Pair<>(topLevel.getKey(), Optional.of(forEachOutput));
    } else {
      allLevels = topLevel;
    }

    return allLevels;
  }
}
