package org.biiig.dmgm.api.algorithms.tfsm;

import org.biiig.dmgm.api.model.collection.GraphCollection;

import java.util.function.Function;

/**
 * Created by peet on 09.08.17.
 */
public interface Operator extends Function<GraphCollection, GraphCollection> {
  void execute(GraphCollection input, GraphCollection output);
}
