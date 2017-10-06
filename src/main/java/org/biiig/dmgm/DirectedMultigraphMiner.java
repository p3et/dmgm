package org.biiig.dmgm;

import org.biiig.dmgm.api.algorithms.tfsm.Algorithm;
import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.impl.algorithms.tfsm.DMGSpan;
import org.biiig.dmgm.impl.algorithms.tfsm.TFSMConfig;

import java.io.IOException;

public class DirectedMultigraphMiner {

  public static void frequentSubgraphs(
    DMGraphCollection inputDatabase,
    DMGraphCollection outputDatabase,
    TFSMConfig config
  ) {

    Algorithm fsm = new DMGSpan(config);

    fsm.execute(inputDatabase, outputDatabase);

  }
}
