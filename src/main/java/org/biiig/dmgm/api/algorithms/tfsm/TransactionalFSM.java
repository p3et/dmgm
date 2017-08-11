package org.biiig.dmgm.api.algorithms.tfsm;

import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.model.graph.DirectedGraph;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by peet on 09.08.17.
 */
public interface TransactionalFSM {
  List<DirectedGraph> mine(Database database, int inputColIdx, int outputColIdx) throws IOException;
}
