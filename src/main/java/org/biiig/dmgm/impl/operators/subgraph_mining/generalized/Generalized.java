package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import javafx.util.Pair;
import org.biiig.dmgm.api.GraphDB;
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
  public Pair<Optional<DFSCodeEmbeddingsPair>, Optional<Consumer<GraphDB>>> apply(DFSCodeEmbeddingsPair supportable) {
    Pair<Optional<DFSCodeEmbeddingsPair>, Optional<Consumer<GraphDB>>> topLevel = filter.apply(supportable);

    Pair<Optional<DFSCodeEmbeddingsPair>, Optional<Consumer<GraphDB>>> allLevels;

    if (topLevel.getValue().isPresent()) {
      // get store update for each specialization
      Collection<Consumer<GraphDB>> outputs = specializer
        .apply(supportable);
      
      // add top level
      outputs.add(topLevel.getValue().get());
      
      // aggregate single consumer
      Consumer<GraphDB> forEachOutput = c -> outputs.forEach(o -> o.accept(c));

      // update consumer
      allLevels = new Pair<>(topLevel.getKey(), Optional.of(forEachOutput));
    } else {
      allLevels = topLevel;
    }

    return allLevels;
  }
}
