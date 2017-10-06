package org.biiig.dmgm.api.algorithms.tfsm;

import org.biiig.dmgm.api.DMGraphDatabase;
import org.biiig.dmgm.api.model.graph.DMGraph;

import java.io.IOException;
import java.util.List;

/**
 * Created by peet on 09.08.17.
 */
public interface TransactionalFSM {
  List<DMGraph> mine(DMGraphDatabase database, int inputColIdx, int outputColIdx) throws IOException;
}
