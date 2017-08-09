package org.biiig.dmgm.api.algorithms.tfsm;

import org.biiig.dmgm.api.Database;

import java.io.IOException;

/**
 * Created by peet on 09.08.17.
 */
public interface TransactionalFSM {
  void mine(Database database, int fromId, int toId) throws IOException;
}
