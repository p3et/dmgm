package org.biiig.dmgm.algorithms.tfsm;

import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.algorithms.tfsm.TransactionalFSM;
import org.biiig.dmgm.api.model.graph.DirectedGraph;
import org.biiig.dmgm.impl.InMemoryDatabase;
import org.biiig.dmgm.impl.algorithms.tfsm.TFSMConfig;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;

import static junit.framework.TestCase.assertEquals;

public abstract class TransactionalFSMTest {

  abstract TransactionalFSM getMiner(TFSMConfig config);

  @Test
  public void mine10() throws Exception {
    mine(1.0f, 702);
  }

  @Test
  public void mine08() throws Exception {
    mine(0.8f, 2106);
  }

  private void mine(float minSupportThreshold, int expectedResultSize) throws IOException {
    TFSMConfig config = new TFSMConfig(minSupportThreshold, 100);
    TransactionalFSM fsm = getMiner(config);
    Database database = new InMemoryDatabase();

    Collection<DirectedGraph> result = fsm.mine(database, 0 , 1);
    assertEquals(expectedResultSize, result.size());
  }

}