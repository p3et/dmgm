package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import javafx.util.Pair;
import org.biiig.dmgm.api.GraphDB;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * A function that outputs a pair with optionally
 * - the input as key, if the input should be fed back to the next iteration
 * - a task for the data store, if something should be stored
 */
public interface FilterOrOutput<T extends Supportable> {
  Pair<Optional<T>, Optional<Consumer<GraphDB>>> apply(T supportable);
}
