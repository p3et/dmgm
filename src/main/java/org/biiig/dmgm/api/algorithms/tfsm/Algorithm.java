package org.biiig.dmgm.api.algorithms.tfsm;

import org.biiig.dmgm.api.model.collection.GraphCollection;

/**
 * Created by peet on 09.08.17.
 */
public interface Algorithm {
  void execute(GraphCollection input, GraphCollection output);
}
