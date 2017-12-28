package org.biiig.dmgm;

import org.biiig.dmgm.api.algorithms.tfsm.Operator;
import org.biiig.dmgm.api.model.collection.GraphCollection;
import org.biiig.dmgm.impl.algorithms.tfsm.FrequentSubgraphs;
import org.biiig.dmgm.impl.algorithms.tfsm.TFSMConfig;

public class DirectedMultigraphMiner {

  public static void frequentSubgraphs(
    GraphCollection inputDatabase,
    GraphCollection outputDatabase,
    TFSMConfig config
  ) {

    Operator fsm = new FrequentSubgraphs();

    fsm.execute(inputDatabase, outputDatabase);

  }
}
