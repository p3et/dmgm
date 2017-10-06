package org.biiig.dmgm.api.algorithms.tfsm;

import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.api.model.graph.DMGraph;

import java.io.IOException;
import java.util.List;

/**
 * Created by peet on 09.08.17.
 */
public interface Algorithm {
  void execute(DMGraphCollection input, DMGraphCollection output);
}
