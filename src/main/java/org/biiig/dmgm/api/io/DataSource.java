package org.biiig.dmgm.api.io;

import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.model.graph.DirectedGraphFactory;

import java.io.IOException;

public interface DataSource {

  void load(
    Database database,
    DirectedGraphFactory graphFactory,
    float minSupportThreshold
  ) throws IOException;
}
